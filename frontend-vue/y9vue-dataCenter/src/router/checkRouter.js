/*
 * @Author: your name
 * @Date: 2021-12-22 14:38:02
 * @LastEditTime: 2024-12-17 15:40:56
 * @LastEditors: chensiwen cikl777@163.com
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: /sz-team-frontend-9.5.x/y9vue-home/src/router/checkRouter.js
 */
import '@/assets/css/nprogress.css'; // progress bar style
import router from '@/router';
import {
	constantRoutes
} from '@/router/index';
import {
	useSettingStore
} from '@/store/modules/settingStore';
import NProgress from 'nprogress'; // progress bar
import {
	$y9_SSO
} from '@/main';


NProgress.configure({
	showSpinner: false,
	easing: 'ease',
	speed: 1000
});



// 路由白名单过滤
function routerWriteList(array, path) {
	let find = false;
	for (let index = 0; index < array.length; index++) {
		const item = array[index];
		if (item.path === path) {
			return true;
		}
		if (item.children) {
			find = routerWriteList(item.children, path);
		}
	}
	if (find) {
		return true;
	} else {
		return false;
	}
}

// 路由白名单
export async function checkWriteList(to, from) {
	// 白名单
	let isWriteList = routerWriteList(constantRoutes, to.path);
	if (isWriteList) {
		if (to.path === '/' || to.path === '/login') {
			// 登陆过 导航直接跳过login页面进入系统
			const isRight = await $y9_SSO.checkToken();
			if ( isRight) {
				window.location =
					import.meta.env.VUE_APP_HOST_INDEX;
			}
		}
		return true;
	} else {
		return false;
	}
}

let userRole = ['user'];
async function check() {
	let  isTokenValid;


	// access_token 是否过期
	isTokenValid = await $y9_SSO.checkToken();
	// console.log(`isTokenValid=${isTokenValid}`);

    if(!isTokenValid) {
        return false;
    }
	
	// redis有记录且token在有效期且角色已获取路由
	if (isTokenValid){
		return true;
	} else {
		if (!isTokenValid) {
			await $y9_SSO.ssoLogout({
				logoutUrl: import.meta.env.VUE_APP_SSO_LOGOUT_URL +
					import.meta.env.VUE_APP_NAME + '/',
			});
		}
		return false;
	}
}



export const routerBeforeEach = async (to, from) => {
	const settingStore = useSettingStore();
	// 进度条
	if (settingStore.getProgress) {
		NProgress.start();
	}
	
	let CHECK = await check();
	if (CHECK) {
		if (!to.name) {
			let array = await router.getRoutes()
			console.log(array, '====')
			array.forEach(item => {
				if (item.path === to.path && item.name) {

					router.push({
						name: item.name
					})
				}
			});
		} else {
			return true;
		}
	}else{
		await $y9_SSO.checkLogin();
	}

	return false;
};
