/*
 * @Author: chensiwen cikl777@163.com
 * @Date: 2024-10-12 15:59:48
 * @LastEditors: chensiwen cikl777@163.com
 * @LastEditTime: 2024-12-17 15:39:59
 * @FilePath: \y9vue-dataCenter\src\router\index.ts
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */

import { createRouter, createWebHistory } from 'vue-router';
import { routerBeforeEach } from '@/router/checkRouter';
import NProgress from 'nprogress';
import articleRouter from './modules/articleRouter';
import searchRouter from './modules/searchRouter';
import y9_storage from '@/utils/storage';
//constantRoutes为不需要动态判断权限的路由，如登录、404、500等
export const constantRoutes: Array<any> = [
    {
        path: '/',
        name: 'index',
        hidden: true,
        redirect: '/search',
    },
    {
        path: '/401',
        hidden: true,
        meta: {
            title: 'Not Permission',
        },
        component: () => import('@/views/401/index.vue'),
    },
    {
        path: '/404',
        hidden: true,
        meta: {
            title: 'Not Found',
        },
        component: () => import('@/views/404/index.vue'),
    },
    articleRouter,
    searchRouter,
];

//创建路由模式，采用history模式没有“#”
const router = createRouter({
    history: createWebHistory(import.meta.env.VUE_APP_PUBLIC_PATH),
    routes: constantRoutes,
});

router.beforeEach(async (to, from, next) => {
    // 路由的前置守卫是否更合理？但是这样单点登录插件不能获取到重定向的URL
    if (to.meta.requiresAuth) {
        // 第一次进来有searchValue 才存储
        let q = parseQueryString(window.location.search.substring(1));
        if (q) {
            // 先存储所有参数，也许有带其它参数可供项目使用到【从安全角度看，可能需要删除和单点登录相关的code和serviceTicketId和state】
            let assgin_q;
            if (to.path == '/search/index') {
                assgin_q = q.wd ? q : {};
            }
            y9_storage.setObjectItem('query', assgin_q);
        } else {
            y9_storage.removeItem('query');
        }
        await routerBeforeEach(to, from);
    }
    next();
});

router.afterEach(() => {
    NProgress.done();
});

function parseQueryString(string) {
    if (string == '') {
        return false;
    }
    var segments = string.split('&').map((s) => s.split('='));
    var queryString = {};
    segments.forEach((s) => (queryString[s[0]] = s[1]));
    return queryString;
}

export default router;
