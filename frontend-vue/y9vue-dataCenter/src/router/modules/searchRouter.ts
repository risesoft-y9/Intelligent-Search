
const searchRouter = {
    path: "/search",
	name:"search",
    redirect: "/search/index",
    component: () => import("@/App.vue"),
    hidden: true,
    meta: {
    	title: "搜索",
    	icon: "ri-home-8-line",
        requiresAuth: JSON.parse(import.meta.env.VUE_APP_SSO_REQUIRESAUTH)
    },
    children:[
        {
            path: "/search/index",
            name:"searchIndex",
            hidden: false,
            meta: {
                title: "搜索",
                icon: "ri-home-8-line",
                requiresAuth: JSON.parse(import.meta.env.VUE_APP_SSO_REQUIRESAUTH)
            },
            component: () => import("@/views/search/index.vue")
        }
    ],
};

export default searchRouter;