<template>
    <div class="table-content">
        <y9Table
            :config="typeTableConfig"
            :filterConfig="filterConfig"
            border
            ref="filterRef"
            @on-curr-page-change="onCurrPageChange"
            @on-page-size-change="onPageSizeChange"
        >
            <template #slotBtns>
                <el-button
                    type="primary"
                    class="global-btn-main"
                    @click="handlerClickSearch"
                    :size="fontSizeObj.buttonSize"
                    :style="{ fontSize: fontSizeObj.baseFontSize }"
                >
                    <i class="ri-search-2-line"></i>{{ $t('查询') }}</el-button
                >
                <el-button
                    class="global-btn-second"
                    @click="handlerClickReset"
                    :size="fontSizeObj.buttonSize"
                    :style="{ fontSize: fontSizeObj.baseFontSize }"
                >
                    <i class="ri-restart-line"></i>{{ $t('重置') }}</el-button
                >
            </template>
        </y9Table>
        <y9Dialog v-model:config="dialogConfig">
            <y9Table :config="dialogTableConfig" border></y9Table>
        </y9Dialog>
        <el-button style="display: none" v-loading.fullscreen.lock="loading"></el-button>
    </div>
</template>

<script lang="ts" setup>
    import { ref, computed, onMounted, inject } from 'vue';
    import { useI18n } from 'vue-i18n';
    // 接口文件 导入
    import { getDataList, getAnnexList, parseMainText, parseAnnex, getDataNameList } from '@/api/data/index'; // index-APIfox
    import { useSettingStore } from '@/store/modules/settingStore';
    const { t } = useI18n();
    const settingStore = useSettingStore();
    // 注入 字体对象
    const fontSizeObj: any = inject('sizeObjInfo');

    interface optionsList {
        label: string;
        value: string;
    }
    // 表格 ref
    let filterRef = ref();

    // 附件列表的id
    let fileListId = ref();

    // 全部类型数组
    const allTypesList = ref([]);

    // 全局loading
    let loading = ref(false);

    onMounted(async () => {
        // 获取类型列表
        await getTypeLists();
        // 列表
        initList();
    });

    // 表格列
    let typeTableConfig = ref({
        headerBackground: true,
        columns: [
            { title: computed(() => t('序号')), type: 'index', width: 90 },
            { title: 'ID', key: 'id' },
            { title: computed(() => t('类型')), key: 'appName' },
            { title: computed(() => t('文件标题')), key: 'biaoti' },
            { title: computed(() => t('登记时间')), key: 'createTime' },
            { title: computed(() => t('同步时间')), key: 'startTime' },
            { title: computed(() => t('同步错误原因')), key: 'error', width: 180 },
        ],
        tableData: [],
        loading: false,
        pageConfig: {
            // 分页配置，false隐藏分页
            currentPage: 1, //当前页数，支持 v-model 双向绑定
            pageSize: 10, //每页显示条目个数，支持 v-model 双向绑定
            layout: 'sizes, prev, pager, next, jumper', //布局
            total: 0, //总条目数
        },
    });

    // 表格过滤表单
    let filterLine = ref({});
    // 表格过滤
    let filterConfig = ref({
        showBorder: true,
        filtersValueCallBack: (filter) => {
            filterLine.value = filter;
        },
        itemList: [
            {
                type: 'input',
                value: '',
                key: 'id',
                label: 'ID',
                span: settingStore.device === 'mobile' ? 12 : 4,
            },
            {
                type: 'input',
                value: '',
                key: 'title',
                label: computed(() => t('标题')),
                span: settingStore.device === 'mobile' ? 12 : 4,
            },
            {
                type: 'select',
                value: '',
                key: 'dataName',
                label: computed(() => t('类型')),
                props: {
                    options: [] as Array<optionsList>,
                },
                span: settingStore.device === 'mobile' ? 12 : 3,
            },
            {
                type: 'slot',
                slotName: 'slotBtns',
                span: settingStore.device === 'mobile' ? 12 : 5,
            },
        ],
    });

    // 获取类型列表
    async function getTypeLists() {
        await getDataNameList()
            .then((result) => {
                if (result.code == 0) {
                    allTypesList.value = result.data?.map((item) => item.dataName);
                    filterConfig.value.itemList?.map((item) => {
                        if (item.key == 'dataName') {
                            item.value = allTypesList.value.join(',');
                            item.props.options = allTypesList.value?.map((item) => {
                                return { label: computed(() => t(item)), value: item };
                            });
                            item.props.options.unshift({
                                label: computed(() => t('全部')),
                                value: allTypesList.value.join(','),
                            });
                        }
                    });
                }
            })
            .catch((err) => console.log(err));
    }

    // 查询 按钮 点击
    function handlerClickSearch() {
        typeTableConfig.value.pageConfig.currentPage = 1;
        initList();
    }

    // 重置 按钮  点击
    function handlerClickReset() {
        filterLine.value = {
            dataName: allTypesList.value.join(','),
        };
        filterRef.value.elTableFilterRef.onReset();
        typeTableConfig.value.pageConfig.currentPage = 1;
        initList();
    }

    // 列表的初始化
    async function initList() {
        typeTableConfig.value.loading = true;
        let params = {
            page: typeTableConfig.value.pageConfig.currentPage,
            limit: typeTableConfig.value.pageConfig.pageSize,
            ...filterLine.value,
        };
        await getDataList(params)
            .then((result) => {
                // 接口返回正确
                if (result.code == 0) {
                    // 列表数据赋值
                    typeTableConfig.value.tableData = result.data.dataList;
                    // total 赋值
                    typeTableConfig.value.pageConfig.total = result.data.total;
                }
            })
            .catch(() => {});
        typeTableConfig.value.loading = false;
    }

    //当前页改变时触发
    function onCurrPageChange(currPage) {
        typeTableConfig.value.pageConfig.currentPage = currPage;
        initList();
    }
    //每页条数改变时触发
    function onPageSizeChange(pageSize) {
        typeTableConfig.value.pageConfig.pageSize = pageSize;
        initList();
    }

    // 弹框的配置
    let dialogConfig = ref({
        show: false,
        title: computed(() => t('附件列表')),
        width: '60%',
        showFooter: false,
    });

    let dialogTableConfig = ref({
        columns: [
            { title: computed(() => t('序号')), type: 'index', width: 90 },
            { title: computed(() => t('附件ID')), key: 'fileguid' },
            { title: computed(() => t('附件名称')), key: 'fileName' },
            { title: computed(() => t('附件类型')), key: 'fileType' },
            { title: computed(() => t('同步错误原因')), key: 'error' },
        ],
        loading: false,
        tableData: [],
        pageConfig: false,
    });
</script>

<style lang="scss" scoped>
    .table-content {
        // 分页样式
        :deep(.y9-pagination) {
            .custom-circular {
                .el-pagination__jump {
                    .el-input__inner {
                        font-size: v-bind('fontSizeObj.baseFontSize');
                    }
                }
                .el-pagination__sizes {
                    margin-right: 16px;
                }
            }
        }
    }
</style>
