/*
 * @Author: your name
 * @Date: 2021-04-15 10:16:53
 * @LastEditTime: 2024-12-17 10:57:11
 * @LastEditors: chensiwen cikl777@163.com
 * @Description: In User Settings Edit
 * @FilePath: \workspace-y9boot-9.5-vuee:\workspace-y9boot-9.6-vue\y9vue-kernel-dcat-style\src\api\lib\request.js
 */
import settings from '@/settings';
import y9_storage from '@/utils/storage';
import axios from 'axios'; // 考虑CDN
import { ElMessage } from 'element-plus';
import { $y9_SSO } from '@/main';

// 创建一个axios实例
function y9Request(baseUrl = '') {
    let requestList = new Set();

    const service = axios.create({
        baseURL: import.meta.env.VUE_APP_CONTEXT,
        withCredentials: false,
        timeout: 0,
    });
    let cancel = null;
    // 请求拦截器(发送请求的时候，携带一些信息)
    service.interceptors.request.use(
        (config) => {
            config.headers['Content-Type'] = 'application/json;charset=UTF-8';
            const access_token =  y9_storage.getObjectItem(settings.siteTokenKey, 'access_token');
            if (access_token) {
                console.log('设置登陆成功的请求头');
                config.headers['Authorization'] = 'Bearer ' + access_token;
            }else {
                config.cancelToken = new axios.CancelToken(function(c) { 
                    cancel = c; 
                });
            }
            // console.log("请求之前设置loading....", config);
            // 在发送请求之前做些什么
            return config;
        },
        (error) => {
            // 处理请求错误
            console.log(error); // for debug
            return Promise.reject(error);
        }
    );

    // 响应拦截器(接收到数据的时候，进行数据过滤、对状态码判断，进行对应的操作)
    service.interceptors.response.use(
        (response) => {
            // console.log("请求响应成功取消loading....", response);
            // 相同请求不得在600毫秒内重复发送，反之继续执行
            setTimeout(() => {
                requestList.delete(response.config.url);
            }, 600);
            let res;
            if (response.data) {
                res = response.data;
            } else {
                res = response;
            }
            const { code } = res;
            if (code !== 0) {
                // 获取替换后的字符串
                const reqUrl = response.config.url.split('?')[0].replace(response.config.baseURL, '');
                const noVerifyBool = settings.ajaxResponseNoVerifyUrl.includes(reqUrl);
                switch (code) {
                    case 100: // 未登陆
                    case 101: // 令牌已失效
                    case 102: // 校验令牌出问题了
                        if (!noVerifyBool) {
                            ElMessageBox({
                                title: t('提示'),
                                showClose: false,
                                closeOnClickModal: false,
                                closeOnPressEscape: false,
                                message: t('当前用户登入信息已失效，请重新登入再操作'),
                                beforeClose: (action, instance, done) => {
                                    if (isExternal(settings.serverLoginUrl)) {
                                        window.location.href = settings.serverLoginUrl;
                                    } else {
                                        const params = {
                                            to: { path: window.location.pathname },
                                            logoutUrl:
                                                import.meta.env.VUE_APP_SSO_LOGOUT_URL +
                                                import.meta.env.VUE_APP_NAME +
                                                '/',
                                            __y9delete__: () => {
                                                // 删除前执行的函数
                                                console.log('删除前执行的函数');
                                            }
                                        };
                                        $y9_SSO.ssoLogout(params);
                                    }
                                    console.log(action, instance, done);
                                }
                            });
                        }

                        break;
                    case 40300:
                        window.location.href = import.meta.env.VUE_APP_PUBLIC_PATH + '/401';
                        break;
                    case 40400:
                        window.location.href = import.meta.env.VUE_APP_PUBLIC_PATH + '/404';
                        break;
                    case 50000:
                        return res;
                    default:
                        if (!noVerifyBool) {
                            console.error(res.msg);
                            // ElMessage({
                            //     message: res.msg || 'Errors',
                            //     type: 'error',
                            //     duration: 1500,
                            // });
                        }
                        break;
                }

                // 返回错误 走 catch
                return Promise.reject(res);
            } else {
                return res;
            }
        },
        (error) => {
            // 异常情况
            if (axios.isCancel(error)) {
                // log
                // 请求取消
                console.warn(error);
                // console.table([error.message.split('---')[0]], 'cancel')
            } else if (error.response) {
                // 请求成功发出且服务器也响应了状态码，但状态代码超出了 2xx 的范围
                requestList.delete(error.config.url);
                let data = error.response.data;
                if (error.response.status === 401 && (data.code === 101 || data.code === 102 || data.code === 100)) {
                    // 令牌已失效（过期或其他标签页单点登出）
                    ElMessageBox({
                        title: t('提示'),
                        showClose: false,
                        closeOnClickModal: false,
                        closeOnPressEscape: false,
                        message: t('当前用户登入信息已失效，请重新登入再操作'),
                        beforeClose: (action, instance, done) => {
                            if (isExternal(settings.serverLoginUrl)) {
                                window.location.href = settings.serverLoginUrl;
                            } else {
                                const params = {
                                    to: { path: window.location.pathname },
                                    logoutUrl:
                                        import.meta.env.VUE_APP_SSO_LOGOUT_URL + import.meta.env.VUE_APP_NAME + '/',
                                    __y9delete__: () => {
                                        // 删除前执行的函数
                                        console.log('删除前执行的函数');
                                    }
                                };
                                $y9_SSO.ssoLogout(params);
                                // window.location.reload();
                            }
                        }
                    });
                } else if (error.response.status === 400) {
                    // 参数、业务上的错误统一返回 http 状态 400，返回原始 body 到请求处自行处理
                    return data;
                }
            }
            return Promise.reject(error);
        } 
       
    );

    return service;
}

export default y9Request;