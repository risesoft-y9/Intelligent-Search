<template>
    <div class="search-term" v-loading.fullscreen.lock="loading" :element-loading-text="loadingText">
        <y9Table
            ref="filterRef"
            v-model:selectedVal="tableCurrSelectedVal"
            :config="tableConfig"
            :filterConfig="filterConfig"
            @on-curr-page-change="onCurrentChange"
            @on-page-size-change="onPageSizeChange"
            @sort-change="handleClickSort"
        >
            <template #searchBtn>
                <el-button
                    :size="fontSizeObj.buttonSize"
                    :style="{ fontSize: fontSizeObj.baseFontSize }"
                    type="primary"
                    class="global-btn-main"
                    @click="initSearchWordsList"
                >
                    <i class="ri-search-2-line"></i>
                    <span>{{ $t('查询') }}</span>
                </el-button>
                <el-button
                    class="global-btn-second"
                    :size="fontSizeObj.buttonSize"
                    :style="{ fontSize: fontSizeObj.baseFontSize }"
                    @click="resetSearchFilter"
                >
                    <i class="ri-restart-line"></i>
                    <span>{{ $t('重置') }}</span>
                </el-button>
            </template>
            <template #operateBtn>
                <el-button
                    class="global-btn-second"
                    :size="fontSizeObj.buttonSize"
                    :style="{ fontSize: fontSizeObj.baseFontSize }"
                    @click="deletesearchWords(false)"
                    :disabled="tableCurrSelectedVal.length == 0"
                >
                    <i class="ri-delete-bin-5-line"></i>
                    <span>{{ $t('删除') }}</span>
                </el-button>
                <el-button
                    class="global-btn-second"
                    :size="fontSizeObj.buttonSize"
                    :style="{ fontSize: fontSizeObj.baseFontSize }"
                    @click="enableOrDisabled(false)"
                    :disabled="tableCurrSelectedVal.length == 0"
                >
                    <i class="ri-shut-down-line"></i>
                    <span>{{ $t('启用/禁用') }}</span>
                </el-button>
                <el-button
                    class="global-btn-second"
                    :size="fontSizeObj.buttonSize"
                    :style="{ fontSize: fontSizeObj.baseFontSize }"
                    @click="handleExport"
                >
                    <i class="ri-share-forward-box-fill"></i>
                    <span>{{ $t('导出') }}</span>
                </el-button>
            </template>
        </y9Table>
    </div>
</template>

<script setup lang="ts">
    import { useI18n } from 'vue-i18n';
    import { useSettingStore } from '@/store/modules/settingStore';
    import { computed, inject, ref, onMounted, reactive, watch, toRefs } from 'vue';
    import { ElMessage, ElMessageBox, ElNotification } from 'element-plus';
    import axios from 'axios';
    import { relatedWordList, deleterelatedWordByIds, enOrDisabledlatedWord } from '@/api/data/index';

    // el-tag样式
    const tagStyle = {
        display: 'inline-flex',
        width: '50px',
        height: '25px',
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: '4px',
        cursor: 'auto',
        fontSize: '14px',
    };

    // 升降序的对应属性值
    let sortValueType = {
        ascending: 'ASC',
        descending: 'DESC',
    };

    const settingStore = useSettingStore();
    // 注入 字体对象
    const fontSizeObj: any = inject('sizeObjInfo');
    const { t } = useI18n();
    const filterRef = ref();
    const data = reactive({
        tableCurrSelectedVal: [], //表格选择的数据
        loading: false, // 全局loading
        loadingText: '',
        currFilters: {}, //当前选择的过滤数据
        tableConfig: {
            //表格配置
            border: false,
            loading: false,
            headerBackground: true,
            columns: [
                {
                    type: 'selection',
                    width: 60,
                },
                {
                    type: 'index',
                    width: 60,
                    title: computed(() => t('序号')),
                },
                {
                    title: computed(() => t('检索词')),
                    key: 'keyword',
                    showOverflowTooltip: false,
                },
                {
                    title: computed(() => t('检索频率')),
                    key: 'frequency',
                    sortable: 'custom',
                    showOverflowTooltip: false,
                },
                {
                    title: computed(() => t('最后修改时间')),
                    key: 'endTime',
                    sortable: 'custom',
                    showOverflowTooltip: false,
                },
                {
                    title: computed(() => t('状态')),
                    key: 'isOpen',
                    showOverflowTooltip: false,
                    render: (row) => {
                        return row.isOpen == 1
                            ? h(
                                  'span',
                                  {
                                      style: {
                                          ...tagStyle,
                                          border: '1px solid var(--el-color-success-light-9)',
                                          color: 'var(--el-color-success)',
                                          backgroundColor: 'var(--el-color-success-light-9)',
                                      },
                                  },
                                  '启用'
                              )
                            : h(
                                  'span',
                                  {
                                      style: {
                                          ...tagStyle,
                                          border: '1px solid var(--el-color-danger-light-9)',
                                          color: 'var(--el-color-danger)',
                                          backgroundColor: 'var(--el-color-danger-light-9)',
                                      },
                                  },
                                  '禁用'
                              );
                    },
                },
                {
                    title: computed(() => t('操作')),
                    with: 200,
                    fixed: 'right',

                    render: (row, params) => {
                        return [
                            h(
                                'span',
                                {
                                    style: {
                                        display: 'inline-flex',
                                        alignItems: 'center',
                                    },
                                    onClick: () => {
                                        enableOrDisabled({ id: row.id });
                                    },
                                },
                                [
                                    h('i', {
                                        class: 'ri-shut-down-line',
                                        style: {
                                            width: '14px',
                                            height: '14px',
                                            lineHeight: '16px',
                                        },
                                    }),
                                    h(
                                        'span',
                                        {
                                            style: {
                                                // verticalAlign: 'middle',
                                                marginLeft: '2px',
                                            },
                                        },
                                        row.isOpen == 1 ? t('禁用') : t('启用')
                                    ),
                                ]
                            ),
                            h(
                                'span',
                                {
                                    style: {
                                        marginLeft: '10px',
                                        display: 'inline-flex',
                                        alignItems: 'center',
                                    },
                                    onclick: async () => {
                                        deletesearchWords({ id: row.id });
                                    },
                                },
                                [
                                    h('i', {
                                        class: 'ri-close-line',
                                        style: {
                                            width: '14px',
                                            height: '14px',
                                            lineHeight: '16px',
                                        },
                                    }),
                                    h(
                                        'span',
                                        {
                                            style: {
                                                verticalAlign: 'middle',
                                                marginLeft: '2px',
                                            },
                                        },
                                        t('删除')
                                    ),
                                ]
                            ),
                        ];
                    },
                },
            ],
            tableData: [
                {
                    word: '供水',
                    status: 1,
                    id: 1,
                },
                {
                    word: '水文',
                    status: 0,
                    id: 2,
                },
            ],
            pageConfig: {
                currentPage: 1, //当前页数，支持 v-model 双向绑定
                pageSize: 20, //每页显示条目个数，支持 v-model 双向绑定
                layout: 'sizes, prev, pager, next, jumper', //布局
                total: 0, //总条目数
                pagerCount: 4,
            },
        },
        filterConfig: {
            itemList: [
                {
                    type: 'search',
                    key: 'keyword',
                    span: 6,
                    label: computed(() => t('检索词')),
                    props: {
                        placeholder: '请输入',
                    },
                },
                {
                    type: 'select', //下拉框
                    key: 'isOpen',
                    value: null,
                    label: '状态', //label
                    span: 4,
                    props: {
                        options: [
                            //选项列表
                            {
                                label: '全部',
                                value: null,
                            },
                            {
                                label: '公开',
                                value: 0,
                            },
                            {
                                label: '非公开',
                                value: 1,
                            },
                        ],
                    },
                },
                {
                    type: 'slot',
                    span: settingStore.device === 'mobile' ? 12 : 7,
                    slotName: 'searchBtn',
                },
                {
                    type: 'slot',
                    justify: 'flex-end',
                    slotName: 'operateBtn',
                    span: settingStore.device === 'mobile' ? 12 : 7,
                },
            ],
            filtersValueCallBack: (filters) => {
                //过滤值回调
                currFilters.value = filters;
            },
        },
        sortProp: null, // 需要排序的值
        sortMethod: null, // 排序的顺序
    });

    let { loading, tableCurrSelectedVal, currFilters, tableConfig, filterConfig, loadingText, sortProp, sortMethod } =
        toRefs(data);

    onMounted(initSearchWordsList);

    // 列表init
    async function initSearchWordsList() {
        tableConfig.value.loading = true;
        let params = {
            ...currFilters.value,
            page: tableConfig.value.pageConfig.currentPage,
            rows: tableConfig.value.pageConfig.pageSize,
            sort: sortProp.value,
            sortOrder: sortMethod.value,
        };
        // 请求接口
        let result = await relatedWordList(params);
        if (result.code == 0) {
            tableConfig.value.tableData = result.data.dataList;
            tableConfig.value.pageConfig.total = result.data.total;
        }
        tableConfig.value.loading = false;
    }

    // 重置
    function resetSearchFilter() {
        filterRef.value.elTableFilterRef.onReset();
        tableConfig.value.pageConfig.currentPage = 1;
        initSearchWordsList();
    }

    // 删除操作
    function deletesearchWords(params?) {
        // console.log(params, 'delete-------------', tableCurrSelectedVal.value);
        ElMessageBox.confirm(t('是否确认删除?'), t('提示'), {
            confirmButtonText: t('确定'),
            cancelButtonText: t('取消'),
            type: 'info',
        })
            .then(async () => {
                loading.value = true;
                let ids = params ? [params.id] : tableCurrSelectedVal.value.map((item) => item.id);
                // console.log(ids, 'ids');
                await deleterelatedWordByIds({ ids: ids.join(',') })
                    .then((res) => {
                        ElNotification({
                            title: res.code == 0 ? t('成功') : t('失败'),
                            message: res.code == 0 ? t('删除成功') : t('删除失败'),
                            type: res.code == 0 ? 'success' : 'error',
                            duration: 2000,
                            offset: 80,
                        });
                        // 重新请求接口
                        initSearchWordsList();
                    })
                    .catch((err) => console.log(err));
                loading.value = false;
            })
            .catch(() => {
                ElMessage({
                    type: 'info',
                    message: t('已取消删除'),
                    offset: 65,
                });
            });
    }

    // 启用/禁用
    function enableOrDisabled(params?) {
        // console.log(params, '0000---------', tableCurrSelectedVal.value);
        ElMessageBox.confirm(t('是否确认对数据进行启用或禁用操作?'), t('提示'), {
            confirmButtonText: t('确定'),
            cancelButtonText: t('取消'),
            type: 'info',
        })
            .then(async () => {
                loading.value = true;
                let ids = params ? [params.id] : tableCurrSelectedVal.value.map((item) => item.id);
                // console.log(ids, 'idsop');
                await enOrDisabledlatedWord({ ids: ids.join(',') })
                    .then((res) => {
                        ElNotification({
                            title: res.code == 0 ? t('成功') : t('失败'),
                            message: res.code == 0 ? t('操作成功') : t('操作失败'),
                            type: res.code == 0 ? 'success' : 'error',
                            duration: 2000,
                            offset: 80,
                        });
                        // 重新请求接口
                        initSearchWordsList();
                    })
                    .catch((err) => console.log(err));
                loading.value = false;
            })
            .catch(() => {
                ElMessage({
                    type: 'info',
                    message: t('已取消操作'),
                    offset: 65,
                });
            });
    }

    // 导出
    async function handleExport() {
        loadingText.value = '正在导出中...';
        loading.value = true;
        const url = import.meta.env.VUE_APP_CONTEXT + 'rest/backstage/exportFrequency';
        await axios
            .get(url, { responseType: 'blob' })
            .then((response) => {
                const blob = new Blob([response.data]);
                const a = document.createElement('a'); //创建a标签
                a.href = window.URL.createObjectURL(blob); // 创建下载的链接
                a.setAttribute('download', '检索词.xlsx'); // 设置下载的文件名
                // a.download = '检索词'; //下载文件名称
                a.style.display = 'none';
                document.body.appendChild(a); //a标签追加元素到body内
                a.click(); //模拟点击下载
                document.body.removeChild(a); // 下载完成移除元素
                window.URL.revokeObjectURL(a.href); // 释放掉blob对象
            })
            .catch(console.error);
        loading.value = false;
        loadingText.value = '';
    }

    // 分页事件
    function handleClickSort(column) {
        sortProp.value = column.prop;
        sortMethod.value = sortValueType[column.order];
        // 请求接口
        initSearchWordsList();
    }

    // 分页操作
    function onCurrentChange(currPage) {
        tableConfig.value.pageConfig.currentPage = currPage;
        initSearchWordsList();
    }

    function onPageSizeChange(pageSize) {
        tableConfig.value.pageConfig.pageSize = pageSize;
        initSearchWordsList();
    }
</script>

<style lang="scss" scoped>
    .search-term {
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
