<!--
 * @Author: your name
 * @Date: 2022-01-10 18:09:52
 * @LastEditTime: 2024-12-24 10:04:49
 * @LastEditors: chensiwen cikl777@163.com
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: /sz- team-frontend-9.6.x/y9vue-home/src/App.vue
-->
<script lang="ts" setup>
    import zhCn from 'element-plus/es/locale/lang/zh-cn';
    import en from 'element-plus/es/locale/lang/en';
</script>

<template>
    <el-config-provider :locale="zhCn">
        <router-view></router-view>
    </el-config-provider>
</template>
