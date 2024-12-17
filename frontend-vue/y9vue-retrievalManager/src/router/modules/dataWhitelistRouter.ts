const dataWhitelistRouter = {
    path: "/dataWhitelist",
    component: () => import("@/layouts/index.vue"),
    redirect: "/dataWhitelist",
	name:"dataWhitelistIndex",
    meta: {
    	title: "数据白名单",
    	icon: "ri-file-list-3-line"
    },
    children: [
    	{
    		path: "/dataWhitelist",
    		component: () => import("@/views/whitelist/index.vue"),
    		name: "dataWhitelist",
    		meta: { title: "数据白名单", icon: "ri-file-list-3-line" },
    	},
		
    ]
};

export default dataWhitelistRouter;