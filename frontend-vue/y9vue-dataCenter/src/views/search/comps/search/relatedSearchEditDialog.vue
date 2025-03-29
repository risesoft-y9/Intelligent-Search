<template>
    <!-- 编辑相关搜索弹窗 -->
    <div class="related-search">
        <y9Dialog v-model:config="dialogConfig">
            <y9Form ref="relatedRef" :config="relatedForm">
                <template #slotRelatedWord>
                    <div class="label-style">
                        <el-tag
                            v-for="tag in dynamicTags"
                            :key="tag.label"
                            :closable="tag.isDelete"
                            class="label-tag"
                            :disable-transitions="false"
                            @close="handleClose(tag)"
                        >
                            {{ tag.label }}
                        </el-tag>
                        <el-input
                            v-if="inputVisible"
                            ref="InputRef"
                            v-model="inputValue"
                            size="small"
                            class="label-input"
                            maxlength="15"
                            @keyup.enter="handleInputConfirm"
                            @blur="handleInputConfirm"
                        />
                        <el-button v-else class="button-new-tag" size="small" @click="showInput"> + </el-button>
                    </div>
                </template>
            </y9Form>
        </y9Dialog>
    </div>
</template>

<script setup lang="ts">
    import { watch, ref, reactive, nextTick } from 'vue';
    import { FormRules, ElMessage, ElMessageBox, ElNotification, ElInput } from 'element-plus';
    import { useSearchStore } from '@/store/modules/searchStore';
    import { saveOrUpdateWords } from '@/api/search/index';
    const searchStore = useSearchStore();

    const props = defineProps({
        // 控制弹框显示
        showDialog: {
            type: Boolean,
            default: false,
        },
        // 弹框内的业务关联词
        relatedData: {
            type: Array,
            default: () => [],
        },
    });

    const emit = defineEmits(['change-dialog-show', 'change-word']);

    /***弹窗 */

    watch(
        () => props.showDialog,
        (newVal) => {
            if (newVal) {
                dialogConfig.value.show = true;
                relatedForm.value.model.word = searchStore.searchFilterInfo.searchContent;
                dynamicTags.value = [...props.relatedData];
            }
        }
    );

    // 搜索词的弹框变量
    let dialogConfig = ref({
        show: false,
        title: '编辑业务词关联',
        width: '50%',
        onOk: (newConfig) => {
            return new Promise(async (resolve, reject) => {
                const ruleFormRef = relatedRef.value.elFormRef;
                if (!ruleFormRef) return;
                await ruleFormRef.validate(async (valid, fields) => {
                    if (valid) {
                        // 通过验证
                        let resultList = dynamicTags.value.map((item) => item.label);
                        let params = {
                            ...relatedRef.value.model,
                            tagNames: resultList.join(','),
                        };

                        // console.log(params, 'params');
                        await saveOrUpdateWords(params)
                            .then(async (res) => {
                                if (res.code == 0) {
                                    // 重新请求相关搜索的列表接口
                                    emit('change-word');
                                    resolve();
                                } else {
                                    reject();
                                }
                                ElNotification({
                                    title: res.code == 0 ? '成功' : '失败',
                                    message: res.code == 0 ? '保存成功' : res.msg,
                                    type: res.code == 0 ? 'success' : 'error',
                                    duration: 2000,
                                    offset: 80,
                                });
                            })
                            .catch((err) => {
                                reject();
                            });
                    } else {
                        reject();
                    }
                });
            });
        },
        visibleChange: (visible) => {
            if (!visible) {
                emit('change-dialog-show');
            }
        },
    });

    /***end结束 */

    /***表单 */

    const relatedRef = ref();

    const rules = reactive<FormRules>({
        word: [
            {
                required: true,
                message: '请输入业务词',
                trigger: 'blur',
            },
        ],
    });

    // FORM label
    let relatedForm = ref({
        model: {
            word: '',
        },
        rules: rules,
        labelWidth: '120px',
        itemList: [
            {
                type: 'input',
                label: '业务词',
                prop: 'word',
                required: true,
                props: {
                    // disabled: true,
                    readonly: true,
                },
            },
            {
                type: 'slot',
                props: {
                    slotName: 'slotRelatedWord',
                },
                label: '业务关联词',
            },
        ],
        descriptionsFormConfig: {
            labelWidth: '200px',
            labelAlign: 'center',
        },
    });

    // 业务关联词列表
    const inputValue = ref('');
    const dynamicTags = ref<Array<any>>([]);
    const inputVisible = ref(false);
    const InputRef = ref<InstanceType<typeof ElInput>>();

    const handleClose = (tag: any) => {
        dynamicTags.value.splice(dynamicTags.value.indexOf(tag.label), 1);
    };

    const showInput = () => {
        inputVisible.value = true;
        nextTick(() => {
            InputRef.value!.input!.focus();
        });
    };

    const handleInputConfirm = () => {
        let matchData = dynamicTags.value?.filter((item) => item.label == inputValue.value.trim());
        if (inputValue.value && matchData.length) {
            ElNotification({
                title: '提示',
                message: '请勿添加重复数据',
                type: 'error',
            });
            return;
        }
        if (inputValue.value) {
            dynamicTags.value.push({ label: inputValue.value.trim(), isDelete: true });
        }
        inputVisible.value = false;
        inputValue.value = '';
    };

    /***结束 */
</script>

<style scoped lang="scss">
    .related-search {
        :deep(.el-button) {
            font-size: 14px;
        }
        // 关联标签  表单
        .label-style {
            border-radius: var(--el-input-border-radius, var(--el-border-radius-base));
            transition: var(--el-transition-box-shadow);
            box-shadow: 0 0 0 1px var(--el-input-border-color, var(--el-border-color)) inset;
            padding: 7px;
            .label-tag {
                margin-right: 10px;
                border-color: var(--el-color-primary);
                border-radius: 3px;
                border-width: 1.5px;
                font-size: 14px;
                height: 25px;
            }
            .button-new-tag {
                min-width: 18px;
                width: 18px;
                height: 18px;
                margin-top: -3px;
                padding: 0;
                border: 1.5px solid var(--el-color-primary);
            }
            .label-input {
                width: 165px;
                :deep(.el-input__wrapper) {
                    box-shadow: 0 0 0 1px var(--el-color-primary, var(--el-color-primary)) inset;
                    height: 28px;
                    .el-input__inner {
                        color: var(--el-color-primary);
                        font-size: 14px;
                    }
                }
            }
            :deep(.el-button) {
                > span {
                    font-size: 18px;
                    margin-top: -3px;
                    color: var(--el-color-primary);
                }
            }
        }
    }
</style>
