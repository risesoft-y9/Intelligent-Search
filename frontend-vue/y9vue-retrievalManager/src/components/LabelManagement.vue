<template>
    <div class="label-style">
        <el-tag
            v-for="tag in labelData"
            :key="tag"
            closable
            class="label-tag"
            :disable-transitions="false"
            @close="handleClose(tag)"
        >
            {{ tag }}
        </el-tag>
        <el-input
            v-if="inputVisible"
            ref="InputRef"
            v-model="inputValue"
            size="small"
            class="label-input"
            @keyup.enter="handleInputConfirm"
            @blur="handleInputConfirm"
        />
        <el-button
            v-else
            class="button-new-tag"
            size="small"
            @click="showInput"
            :size="fontSizeObj.buttonSize"
            :style="{ fontSize: fontSizeObj.baseFontSize }"
        >
            +
        </el-button>
    </div>
</template>

<script setup lang="ts">
    import { inject, watch, ref, nextTick } from 'vue';
    // 注入 字体对象
    const fontSizeObj: any = inject('sizeObjInfo');

    // props
    const props = defineProps({
        // 初始标签数组
        initLabelList: {
            type: Array,
            default: () => [],
        },
    });

    // emit
    const emit = defineEmits(['change-list']);

    // 标签数组
    const labelData = ref<Array<any>>([]);
    // 输入框值
    const inputValue = ref('');
    // 控制输入框显示
    const inputVisible = ref(false);
    // input的ref
    const InputRef = ref();

    watch(
        () => props.initLabelList,
        (newList) => {
            labelData.value = newList;
        },
        {
            deep: true,
            immediate: true,
        }
    );

    const handleClose = (tag: string) => {
        labelData.value?.splice(labelData.value.indexOf(tag), 1);
    };

    const showInput = () => {
        inputVisible.value = true;
        nextTick(() => {
            InputRef.value!.input!.focus();
        });
    };

    const handleInputConfirm = () => {
        if (inputValue.value) {
            labelData.value.push(inputValue.value.trim());
            emit('change-list', labelData.value);
        }
        inputVisible.value = false;
        inputValue.value = '';
    };
</script>

<style scoped lang="scss">
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
            font-size: v-bind('fontSizeObj.baseFontSize');
            height: 28px;
        }
        .button-new-tag {
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
                    font-size: v-bind('fontSizeObj.baseFontSize');
                }
            }
        }
        :deep(.el-button) {
            > span {
                font-size: v-bind('fontSizeObj.largeFontSize');
                margin-top: -3px;
                color: var(--el-color-primary);
            }
        }
    }
</style>
