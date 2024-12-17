const articleRouter = {
    path: "/article",
    name:"article",
    component: () => import("@/views/search/article.vue"),
	hidden: true,
    meta: {
        requiresAuth: JSON.parse(import.meta.env.VUE_APP_SSO_REQUIRESAUTH)
    }
};

export default articleRouter;