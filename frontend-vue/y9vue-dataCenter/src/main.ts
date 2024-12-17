/*
 * @Author: your name
 * @Date: 2022-01-10 18:09:52
 * @LastEditTime: 2024-11-15 16:01:04
 * @LastEditors: chensiwen cikl777@163.com
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: /sz- team-frontend-9.6.x/y9vue-home/src/main.js
 */
import router from '@/router/index';
import { setupStore } from '@/store';
import 'animate.css';
import 'normalize.css'; // 样式初始化
import 'remixicon/fonts/remixicon.css';
import { createApp } from 'vue';
// import sso from "y9plugin-sso"
import App from './App.vue';
import './theme/global.scss';

//有生云公共组件库
import y9pluginComponents from 'y9plugin-components';

import customDirective from '@/utils/directive';

import sso from 'y9plugin-sso';
const env = {
    sso: {
        VUE_APP_SSO_DOMAINURL: import.meta.env.VUE_APP_SSO_DOMAINURL, // sso接口
        VUE_APP_SSO_CONTEXT: import.meta.env.VUE_APP_SSO_CONTEXT, // sso接口上下文
        VUE_APP_SSO_AUTHORIZE_URL: import.meta.env.VUE_APP_SSO_AUTHORIZE_URL, //sso授权码接口
        VUE_APP_SSO_LOGOUT_URL: import.meta.env.VUE_APP_SSO_LOGOUT_URL, //退出URL
        VUE_APP_SSO_CLIENT_ID: import.meta.env.VUE_APP_SSO_CLIENT_ID, //sso接口的固定字段
        VUE_APP_SSO_SECRET: import.meta.env.VUE_APP_SSO_SECRET, //sso接口的固定字段
        VUE_APP_SSO_GRANT_TYPE: import.meta.env.VUE_APP_SSO_GRANT_TYPE, //sso接口的固定字段
        VUE_APP_SSO_SITETOKEN_KEY: import.meta.env.VUE_APP_SSO_SITETOKEN_KEY, //sso-token_key
    },
    logInfo: {
        showLog: true,
    },
};

const app: any = createApp(App);

setupStore(app);
app.use(customDirective);
app.use(y9pluginComponents);

app.use(sso, { env });
app.use(router); //路由
app.mount('#app'); //挂载app.vue

export const $y9_SSO = app.$y9_SSO;
