const dataSupervisionRouter = {
    path: "/dataSupervision",
    component: () => import("@/layouts/index.vue"),
    redirect: "/dataSupervision",
	name:"dataSupervisionIndex",
    meta: {
    	title: "数据流监管",
    	icon: "ri-line-chart-line"
    },
    children: [
    	{
    		path: "/dataSupervision",
    		component: () => import("@/views/supervision/index.vue"),
    		name: "dataSupervision",
    		meta: { title: "数据流监管", icon: "ri-line-chart-line" },
    	},
		
    ]
};

export default dataSupervisionRouter;