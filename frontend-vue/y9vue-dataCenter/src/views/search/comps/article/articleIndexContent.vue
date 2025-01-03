<!--
 * 文章页面的中心内容
-->

<template>
    <div class="content-center" v-loading="loading">
        <!-- 标题 -->
        <div class="content-center-title">
            <div class="title">{{ fileInfoObj?.title }}</div>
            <div class="title-text-info">
                <span>类型：{{ fileInfoObj.dataType }}</span>
                <span>日期：{{ fileInfoObj.dataTime }}</span>
                <span>浏览次数：{{ fileInfoObj.clickNum }}</span>
            </div>
        </div>
        <!-- 基本信息 -->
        <div class="content-base-info">
            <div class="base-info-title">
                <div class="title-info">
                    <i class="ri-play-mini-fill"></i>
                    <span>基本信息</span>
                </div>
                <el-divider />
            </div>
            <el-row>
                <template v-for="item in fileInfoObj.freeFields">
                    <!-- :span="maxLengthElement.includes(item.name) ? 22 : 11" -->
                    <el-col :span="11" v-if="item.content != null && item.content != ''">
                        <span :class="{ 'large-span': item.name.length > 7 }">{{ item.name }}</span>
                        <span :class="{ 'large-span2': item.name.length > 7 }">{{ item.content }}</span>
                    </el-col>
                </template>
            </el-row>
            <div class="base-info-title" style="margin-top: 10px">
                <div class="title-info">
                    <i class="ri-play-mini-fill"></i>
                    <span>文件详情</span>
                </div>
                <el-divider />
            </div>
            <el-row>
                <template v-if="fileInfoObj?.content != null && fileInfoObj?.content != ''">
                    <el-col :span="22" style="display: flex; margin-left: 26px">
                        <span v-if="fileInfoObj?.content.length > 200" class="document-content" id="file-info">
                            <span v-if="!showAbstract">
                                <span>{{ fileInfoObj?.content.substr(0, shrinkLength) }}...</span>
                                <span class="click-on-span" @click="handlerClickOn">[展开]</span>
                            </span>
                            <span v-else>
                                <span>{{ fileInfoObj?.content }} </span>
                                <span class="click-on-span" @click="handlerClickOn">[收起]</span>
                            </span>
                        </span>
                        <span v-else>
                            {{ fileInfoObj?.content }}
                        </span>
                    </el-col>
                </template>
            </el-row>
        </div>
        <!-- 附件 -->
        <div class="content-enclosure" v-if="fileInfoObj?.attachments?.length > 0">
            <div class="base-info-title">
                <div class="title-info">
                    <i class="ri-play-mini-fill"></i>
                    <span>附件</span>
                </div>
                <el-divider />
            </div>
            <el-row class="file-row">
                <el-col :span="18">
                    <span>名称</span>
                </el-col>
                <el-col :span="6">
                    <span class="file-operation">操作</span>
                </el-col>
            </el-row>
            <el-divider />
            <!-- 附件 -->
            <el-row v-for="(item, index) in fileInfoObj?.attachments" :key="index" class="file-row">
                <el-col :span="18">
                    <el-tooltip :content="item.fileName" placement="top" effect="light" :show-after="1000">
                        <div class="file-list">
                            <div class="file-name">
                                <i :class="getFileTypeIcon(item.fileType)"></i>
                                <span>{{ item.fileName }}</span>
                            </div>
                        </div>
                    </el-tooltip>
                </el-col>
                <el-col :span="6">
                    <div class="btns">
                        <span
                            class="preview"
                            @click="onPreviewFile(fileInfoObj?.id, item.fileName, item.fileUrl, item.fileType)"
                            >预览</span
                        >
                        <span @click="downloadFile(item.fileName, item.fileUrl)">下载</span>
                    </div>
                </el-col>
                <el-divider />
            </el-row>
        </div>
    </div>
</template>

<script lang="ts" setup>
    import { onMounted, ref } from 'vue';
    import { useRoute } from 'vue-router';
    import { openFile } from '@/api/search/index';
    import { replaceGoodWords } from '@/utils/index';
    import axios from 'axios';
    import { downloadLog } from '@/api/search/index'; // index-APIfox

    // 实例化路由
    const route = useRoute();

    // 定义变量值 info
    let fileInfoObj = ref({});
    let loading = ref(false);

    // 文件能预览的类型总合
    let allFileType = ['doc', 'docx', 'xlsx', 'xls', 'ppt', 'pptx', 'txt', 'wps', 'ofd', 'jpg', 'png', 'pdf'];

    const emits = defineEmits(['getTitle']);

    onMounted(async () => {
        await getFileInfo();
    });

    // fields里面需要span为22的字段
    let maxLengthElement = [
        '处理结果',
        '工单类型',
        '问题点位',
        '地址',
        '不计入考评',
        '不计入考评原因',
        '提示',
        '项目名称',
        '申请材料目录',
        '注意事项',
        '抽查事项',
        '检查结果',
        '检查结果描述',
        '备注',
        '营业执照号码或组织机构代码',
    ];

    //摘要|正文 的收缩长度
    const shrinkLength = 200;

    // 展开更多
    let showAbstract = ref(false);
    // 点击展开，展示更多摘要
    function handlerClickOn() {
        showAbstract.value = !showAbstract.value;
    }

    // 内页的数据请求
    async function getFileInfo() {
        loading.value = true;
        let params = {
            id: route.query.id,
            dataType: route.query.dataType,
            keyword: route.query.keyword,
        };

        const res = await openFile(params);
        if (res.code == 0) {
            fileInfoObj.value = await replaceGoodWords(res.data);
        }

        emits('getTitle', fileInfoObj.value, route.query.keyword);

        loading.value = false;
    }

    // 文件的图标
    function getFileTypeIcon(fileType) {
        switch (fileType) {
            case 'pdf':
                return 'ri-file-pdf-line';
            case 'wps':
            case 'word':
            case 'doc':
            case 'docx':
                return 'ri-file-word-line';
            case 'ppt':
            case 'pptx':
                return 'ri-file-ppt-line';
            case 'xlsx':
            case 'xls':
                return 'ri-file-excel-line';
            default:
                return 'ri-file-3-line';
        }
    }

    //下载文件
    function downloadFile(fileName, fileUrl) {
        // axios.get(fileUrl, { responseType: "blob" }).then(response => {
        //    const blob = new Blob([response.data]);
        //    const a = document.createElement("a");//创建a标签
        //    a.href = window.URL.createObjectURL(blob);// 创建下载的链接
        //    a.download = fileName;//下载文件名称
        //    a.style.display = "none";
        //    document.body.appendChild(a);//a标签追加元素到body内
        //    a.click();//模拟点击下载
        //    document.body.removeChild(a);// 下载完成移除元素
        //     window.URL.revokeObjectURL(a.href);// 释放掉blob对象
        // }).catch(console.error);

        // const a = document.createElement('a');//创建a标签
        // a.style.display = "none"//设置样式
        // a.href = fileUrl;//下载链接
        // a.download = fileName;//文件名称
        // document.body.appendChild(a)//添加a标签到body
        // a.click();//模拟点击a标签
        // document.body.removeChild(a)//移除a标签
        const url =
            import.meta.env.VUE_APP_CONTEXT +
            '/rest/officeInfo/download?name=' +
            encodeURI(fileName) +
            '&url=' +
            encodeURIComponent(fileUrl);
        axios
            .get(url, { responseType: 'blob' })
            .then((response) => {
                const blob = new Blob([response.data]);
                const a = document.createElement('a'); //创建a标签
                a.href = window.URL.createObjectURL(blob); // 创建下载的链接
                a.download = fileName; //下载文件名称
                a.style.display = 'none';
                document.body.appendChild(a); //a标签追加元素到body内
                a.click(); //模拟点击下载
                document.body.removeChild(a); // 下载完成移除元素
                window.URL.revokeObjectURL(a.href); // 释放掉blob对象
            })
            .catch((error) => console.log(error));
        downloadLog('', fileUrl, fileName);
    }

    //预览文件
    function onPreviewFile(id, fileName, url, fileType) {
        // let isMate = allFileType.filter(item => item === fileType);

        // if(!isMate.length || fileType == '正文') {
        //     ElNotification({
        // 		title: '预览失败',
        // 		message: '该类型文件不可被预览，需下载查看',
        // 		type:  'error',
        // 		duration: 2000,
        // 		offset: 80,
        // 	});
        //     return;
        // }

        // 1-用.分割
        let fileNameArr = fileName.split('.');
        // 2-取数组最后一个元素
        let fileType2 = fileNameArr[fileNameArr.length - 1];
        let isMate = allFileType.filter((item) => item === fileType2);
        // 判断是否是符合预览的条件类型
        if (!isMate.length) {
            ElNotification({
                title: '预览失败',
                message: '该类型文件不可被预览，需下载查看',
                type: 'error',
                duration: 2000,
                offset: 80,
            });
            return;
        }

        if (route.query.keyword == null || route.query.keyword == undefined || route.query.keyword == '') {
            window.open(
                import.meta.env.VUE_APP_HOST +
                    '/plugin/pdfjs/web/viewer.html?file=' +
                    encodeURIComponent(
                        import.meta.env.VUE_APP_CONTEXT +
                            '/pdf/localPdf?id=' +
                            encodeURI(id) +
                            '&fileName=' +
                            encodeURI(fileName) +
                            '&fileUrl=' +
                            encodeURIComponent(url)
                    )
            );
        } else {
            window.open(
                import.meta.env.VUE_APP_HOST +
                    '/plugin/pdfjs/web/viewer.html?file=' +
                    encodeURIComponent(
                        import.meta.env.VUE_APP_CONTEXT +
                            '/pdf/localPdf?id=' +
                            encodeURI(id) +
                            '&fileName=' +
                            encodeURI(fileName) +
                            '&fileUrl=' +
                            encodeURIComponent(url)
                    ) +
                    '&keyword=' +
                    route.query.keyword
            );
        }
        downloadLog(id, url, fileName);
    }
</script>
<style scoped lang="scss">
    @import '@/theme/global.scss';
    .content-center {
        height: calc(100vh - 60px - 60px);
        background-color: #fff;
        padding: 0 30px;
        margin-right: 30px;
        box-shadow: $boxShadow;
        overflow: auto;
        border-radius: 5px;
        .base-info-title {
            .title-info {
                display: flex;
                align-items: center;
                padding-bottom: 4px;
                i {
                    font-size: 22px;
                }
                span {
                    font-size: 18px;
                    font-weight: 600;
                    font-family: 'Microsoft YaHei';
                }
            }
            .document-content {
                width: calc(100% - 140px);
                > span:nth-child(1) {
                    display: table-cell;
                    color: #333;
                    opacity: 0.9;
                    line-height: 18px;
                }
            }
        }
        .content-center-title {
            padding-top: 30px;
            display: flex;
            flex-direction: column;
            align-items: center;
            .title {
                font-size: 22px;
                font-weight: 600;
                margin-bottom: 20px;
                text-align: center;
            }
            .title-text-info {
                color: #999;
                opacity: 0.7;
                font-size: 14px;
                margin-bottom: 20px;
                display: flex;
                flex-wrap: wrap;
                justify-content: center;
                line-height: 20px;
                width: 80%;
                > span {
                    margin-right: 20px;
                }
            }
        }
        .content-base-info {
            :deep(.el-col) {
                margin-top: 20px;
                margin-bottom: 10px;
                > span:nth-child(1) {
                    display: table-cell;
                    white-space: nowrap;
                    color: var(--el-color-primary);
                    opacity: 0.8;
                    width: 140px;
                    text-align: center;
                }
                > span:nth-child(2) {
                    width: calc(100% - 140px);
                    display: table-cell;
                    color: #333;
                    opacity: 0.9;
                }
                .large-span {
                    width: 256px !important;
                }
                .large-span2 {
                    width: calc(100% - 256px) !important;
                }
                .document-content {
                    width: calc(100% - 140px);
                    span:nth-child(1) {
                        color: #333;
                        opacity: 0.9;
                        line-height: 22px;
                    }
                }
                .click-on-span {
                    color: var(--el-color-primary);
                    cursor: pointer;
                }
                #file-info {
                    white-space: normal;
                    width: 100%;
                    text-align: left;
                }
            }
        }
        .content-enclosure {
            margin-bottom: 50px;
            margin-top: 10px;
            .file-row {
                height: 50px;
                align-items: center;
                color: var(--el-color-primary-light-3);
                :deep(.el-col) {
                    > span {
                        margin-left: 60px;
                    }
                    > div {
                        margin-left: 36px;
                    }
                    .file-list {
                        display: flex;
                        flex-direction: column;
                        justify-content: center;
                        height: 50px;
                        .search-key-list {
                            display: flex;
                            color: #999;
                            font-size: 12px;
                            margin-top: 6px;
                            margin-left: 18px;
                            .search-key-item {
                                margin-right: 5px;
                                // cursor: pointer;
                            }
                        }
                    }
                    .file-name {
                        width: 85%;
                        display: flex;
                        align-items: center;
                        font-size: 16px;
                        color: var(--el-color-primary-light-3);
                        cursor: pointer;
                        i {
                            font-size: 18px;
                            margin-right: 4px;
                            flex-shrink: 0;
                            line-height: 18px;
                        }
                        > span {
                            width: 100%;
                            @include textEllipsis;
                        }
                    }
                    .btns {
                        .preview {
                            margin-right: 50px;
                        }
                        span {
                            cursor: pointer;
                        }
                    }
                    .file-operation {
                        margin-left: 77px;
                    }
                }
            }
        }
    }

    :deep(.el-divider--horizontal) {
        margin: 0;
    }
</style>
