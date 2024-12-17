<template>
    <div v-if="appName" class="title-name">
        <span>{{ appName }}</span>
    </div>
    <div class="search-list" v-if="dataList.length">
        <div v-for="(item, index) in dataList" :key="index" class="search-list-item">
            <searchListItem :listItem="item" />
        </div>
    </div>
    <div style="margin-bottom: 20px;height: 50px;">
        <y9Pagination v-if="dataList.length > 0" :config="paginationInfo" 
        @current-change="onCurrentPageChange"
        @size-change="onPageSizeChange"></y9Pagination>
    </div>
</template>

<script lang="ts" setup>
// 搜索列表 item组件
import { watch, ref } from 'vue';
import searchListItem from './searchListItem.vue';
const props = defineProps({
    // 是否是分类列表 且有对应的数据类型
    appName: {
        type: String,
        default: '',
    },
    // 数组数据
    dataList: {
        type: Array,
        default: () => []
    },
    // 分页的数据
    paginationInfo: {
        type: Object,
        default: () => {}
    },
})

const emits = defineEmits(['on-currpage', 'on-pagesize']);

watch(() => props.paginationInfo, (newVal) => {
    setTimeout(() => {
        getPaginationInfo(newVal);
    }, 1000)

},{
    deep: true,
    immediate: true,
})

function onCurrentPageChange(currPage) {
    emits('on-currpage', currPage)
}


function onPageSizeChange(pageSize) {
    emits('on-pagesize', pageSize)

}

// 分页
function getPaginationInfo(newVal: any) {
    let pagerELement: any = document.getElementsByClassName('el-pager');
    pagerELement = [].slice.apply(pagerELement);
    pagerELement?.map(item =>{
        let numberElements: any = item.getElementsByClassName('number');
        numberElements = [].slice.apply(numberElements);

        let morePrevElement: any = item.getElementsByClassName('btn-quickprev');
        morePrevElement = [].slice.apply(morePrevElement);

        let moreNextElement: any = item.getElementsByClassName('btn-quicknext');
        moreNextElement = [].slice.apply(moreNextElement);

        if(moreNextElement[0]) {
            moreNextElement[0].style.display = 'none';
            numberElements[numberElements.length - 1].style.display = 'none';
        }else if(newVal.currentPage > Math.ceil(newVal.total / newVal.pageSize) - Math.ceil(newVal.pagerCount / 2)){
            numberElements[numberElements.length - 1].style.display = 'flex';
        }

        if(morePrevElement[0]) {
            morePrevElement[0].style.display = 'none';
            numberElements[0].style.display = 'none';
        }else if(newVal.currentPage <  Math.ceil(newVal.pagerCount / 2) + 1) {
            numberElements[0].style.display = 'flex';
        }
    })
    

}


</script>

<style lang="scss" scoped>
@import '@/theme/global.scss';
.title-name {
    height: 30px;
    background: var(--el-color-primary);
    border-radius: 4px;
    display: flex;
    align-items: center;
    padding-left: 10px;
    margin-bottom: 15px;
    >span {
        color: #fff;
    }
}

.search-list-item {
    background-color: var(--el-bg-color);
    box-shadow: $boxShadow;
    border-radius: 5px;
}



</style>