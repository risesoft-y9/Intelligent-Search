const searchTermManageRouter = {
    path: '/searchTermManagement',
    component: () => import('@/layouts/index.vue'),
    redirect: '/searchTermManagement',
    name: 'searchTermManagementIndex',
    meta: {
        title: '检索词管理',
        icon: 'ri-file-search-line',
    },
    children: [
        {
            path: '/searchTermManagement',
            component: () => import('@/views/searchTermManagement/index.vue'),
            name: 'searchTermManagement',
            meta: { title: '检索词管理', icon: 'ri-file-search-line' },
        },
    ],
};

export default searchTermManageRouter;
