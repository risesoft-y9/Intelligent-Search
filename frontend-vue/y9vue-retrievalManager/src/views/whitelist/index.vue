<template>
    <fixedTreeModule
        ref="fixedTreeRef"
        :treeApiObj="treeApiObj"
        @onTreeClick="onTreeClick"
        nodeLabel="dataName"
        :hiddenSearch="true"
    >
        <template #actions="{ item }">
            <el-switch
                @change="(value) => handlerSwitchChange(value, item)"
                v-model="item.isShow"
                style="--el-switch-on-color: var(--el-color-primary); height: 26px"
            />
        </template>
        <template #rightContainer>
            <template v-if="Object.keys(currData).length > 0">
                <y9Card :title="`${currData.dataName ? $t(currData.dataName) : ''}`" class="card-content">
                    <y9Table
                        :config="typeTableConfig"
                        :filterConfig="filterConfig"
                        border
                        @on-curr-page-change="onCurrPageChange"
                        @on-page-size-change="onPageSizeChange"
                    >
                        <template #slotBtns>
                            <el-button
                                type="primary"
                                @click="handlerClickSearch"
                                :size="fontSizeObj.buttonSize"
                                :style="{ fontSize: fontSizeObj.baseFontSize }"
                            >
                                <i class="ri-search-2-line"></i>{{ $t('查询') }}</el-button
                            >
                        </template>
                        <template #switchCell="{ row, index }">
                            <el-switch
                                v-model="isOpenData[index]"
                                @change="(value) => handlerTableSwitchChange(value, row, index)"
                                style="--el-switch-on-color: var(--el-color-primary); height: 26px"
                            />
                        </template>
                    </y9Table>
                </y9Card>
            </template>
        </template>
        <el-button style="display: none" v-loading.fullscreen.lock="loading"></el-button>
    </fixedTreeModule>
</template>

<script lang="ts" setup>
    import { ref, computed, nextTick, inject } from 'vue';
    import { useI18n } from 'vue-i18n';
    // 接口文件 导入
    import { getDataList, openChange, typeNameSwitch, getDataNameList } from '@/api/data/index';
    import fixedTreeModule from '@/components/pageModule/fiexedTreeModule.vue';
    import { useSettingStore } from '@/store/modules/settingStore';
    const { t } = useI18n();
    const settingStore = useSettingStore();
    // 注入 字体对象
    const fontSizeObj: any = inject('sizeObjInfo');
    // 全局loading
    let loading = ref(false);

    // 树ref
    const fixedTreeRef = ref();
    // 树的一级 子级, 搜索的请求接口函数
    const treeApiObj = ref({
        topLevel: getDataNameList,
    });

    // 数据类型
    let dataName = ref('');

    // 点击树节点 对应数据的载体
    let currData: any = ref({});
    // 点击树  拿到对应数据
    function onTreeClick(currTreeNode) {
        // 将拿到的数据 里的id赋值给 系统id变量
        currData.value = currTreeNode;
        // 请求接口
        dataName.value = currTreeNode.dataName;
        typeTableConfig.value.pageConfig.currentPage = 1;
        initList();
    }

    // 点击switch开关
    async function handlerSwitchChange(value, item) {
        nextTick(() => {
            fixedTreeRef.value.y9TreeRef.setCurrentKey(currData.value.id);
        });
        ElMessageBox.confirm(t('是否切换状态'), t('提示'), {
            confirmButtonText: t('确定'),
            cancelButtonText: t('取消'),
            type: 'info',
        })
            .then(async () => {
                loading.value = true;
                await typeNameSwitch({ id: item.id })
                    .then((result) => {
                        // 提示信息
                        ElNotification({
                            title: result.code == 0 ? t('成功') : t('失败'),
                            message: result.code == 0 ? t('状态切换成功') : result.msg,
                            type: result.code == 0 ? 'success' : 'error',
                            duration: 2000,
                            offset: 80,
                        });
                        // 不等于0 未能成功切换，恢复原状态
                        if (result.code !== 0) {
                            item.isShow = !item.isShow;
                            nextTick(() => {
                                fixedTreeRef.value.y9TreeRef.setCurrentKey(currData.value.id);
                            });
                        }
                    })
                    .catch(() => {});
                loading.value = false;
            })
            .catch(() => {
                ElMessage({
                    type: 'info',
                    message: t('已取消切换'),
                    offset: 65,
                });
                // 取消切换，恢复原状态
                item.isShow = !item.isShow;
                nextTick(() => {
                    fixedTreeRef.value.y9TreeRef.setCurrentKey(currData.value.id);
                });
            });
    }

    // 右侧类型的表格列
    let typeTableConfig = ref({
        columns: [
            { title: 'ID', key: 'id' },
            { title: computed(() => t('文件标题')), key: 'biaoti' },
            { title: computed(() => t('数据时间')), key: 'createTime' },
            {
                title: computed(() => t('是否公开')),
                showOverflowTooltip: false,
                slot: 'switchCell',
            },
        ],
        loading: false,
        tableData: [],
        pageConfig: {
            // 分页配置，false隐藏分页
            currentPage: 1, //当前页数，支持 v-model 双向绑定
            pageSize: 10, //每页显示条目个数，支持 v-model 双向绑定
            layout: 'sizes, prev, pager, next, jumper', //布局
            total: 0, //总条目数
            pagerCount: 4,
        },
    });

    // 用于显示表格的 是否开启
    let isOpenData = ref([]);

    // 表格过滤表单
    let filterLine = ref({});
    // 右侧类型的表格过滤
    let filterConfig = ref({
        showBorder: true,
        filtersValueCallBack: (filter) => {
            filterLine.value = filter;
        },
        itemList: [
            {
                type: 'input',
                value: '',
                key: 'title',
                label: computed(() => t('文件标题')),
                span: settingStore.device === 'mobile' ? 24 : 7,
            },
            {
                type: 'select',
                value: '',
                key: 'isOpen',
                label: computed(() => t('是否公开')),
                props: {
                    options: [
                        {
                            label: computed(() => t('全部')),
                            value: '',
                        },
                        {
                            label: computed(() => t('公开')),
                            value: 'open',
                        },
                        {
                            label: computed(() => t('未公开')),
                            value: 'notOpen',
                        },
                    ],
                },
                span: settingStore.device === 'mobile' ? 15 : 7,
            },
            {
                type: 'slot',
                slotName: 'slotBtns',
                span: settingStore.device === 'mobile' ? 9 : 5,
            },
        ],
    });

    // 列表的查询按钮 点击
    function handlerClickSearch() {
        typeTableConfig.value.pageConfig.currentPage = 1;
        initList();
    }

    // 列表的初始化
    async function initList() {
        typeTableConfig.value.loading = true;
        let params = {
            page: typeTableConfig.value.pageConfig.currentPage,
            limit: typeTableConfig.value.pageConfig.pageSize,
            dataName: dataName.value,
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
                    // 自己定义的open列表赋值
                    isOpenData.value = result.data.dataList.map((item) => {
                        return item.isOpen == 'open' ? true : false;
                    });
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

    // 表格的 switch change
    async function handlerTableSwitchChange(value, row, index) {
        ElMessageBox.confirm(t('是否切换状态'), t('提示'), {
            confirmButtonText: t('确定'),
            cancelButtonText: t('取消'),
            type: 'info',
        })
            .then(async () => {
                loading.value = true;
                await openChange({ id: row.id })
                    .then((result) => {
                        // 提示信息
                        ElNotification({
                            title: result.code == 0 ? t('成功') : t('失败'),
                            message: result.code == 0 ? t('状态切换成功') : result.msg,
                            type: result.code == 0 ? 'success' : 'error',
                            duration: 2000,
                            offset: 80,
                        });
                        // 接口未成功 恢复原状态
                        if (result.code !== 0) {
                            isOpenData.value[index] = !isOpenData.value[index];
                        }
                        // 请求列表接口
                        //initList();
                    })
                    .catch(() => {});
                loading.value = false;
            })
            .catch(() => {
                ElMessage({
                    type: 'info',
                    message: t('已取消切换'),
                    offset: 65,
                });
                // 取消切换 恢复原状态
                isOpenData.value[index] = !isOpenData.value[index];
            });
    }
</script>

<style lang="scss" scoped>
    // switch 开关
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
</style>
