<template> 
    <div :class="{ 'primary': primary, 'plain': !primary}" class="btns">
        <i v-if="iconClass" :class="iconClass"></i> {{ btnName }}
    </div>
</template>

<script lang="ts" setup>

const props = defineProps({
    // 图标
    iconClass: {
        type: String,
    },
    btnName: {
        type: String
    },
    // 是否是主按钮
    primary: {
        type: Boolean,
        default: true
    }
})

</script>

<style lang="scss" scoped>
.btns {
    width: 68px;
    height: 30px;
    border-radius: 4px;
    font-size: 14px;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    margin-left: 18px;
    box-shadow: 2px 2px 2px 1px rgb(0 0 0 / 6%);
    user-select: none;
    i{
        margin-right: 4px;
    }
}
.primary {
    background-color: var(--el-color-primary);
    color: #fff;
}
.primary:hover {
    color: #fff;
    border-color: var(--el-color-primary-light-3);
    background-color: var(--el-color-primary-light-3);
    outline: none;
}

.plain {
    box-shadow: 2px 2px 2px 1px rgb(0 0 0 / 6%);
    background-color: var(--el-color-primary-light-9);
    border:1px solid var(--el-color-primary-light-5);
    color: var(--el-color-primary);
    box-sizing: border-box;
}

.plain:hover {
    background-color: var(--el-color-primary);
    border: 1px solid var(--el-color-primary);
    color: #fff;
} 


</style>