const operationLogRouter = {
    path: '/operationLogs',
    component: () => import('@/layouts/index.vue'),
    redirect: '/operationLogs',
    name: 'operationLogsIndex',
    meta: {
        title: '操作日志',
        icon: 'ri-file-lock-line',
    },
    children: [
        {
            path: '/operationLogs',
            component: () => import('@/views/operationLog/index.vue'),
            name: 'operationLogs',
            meta: { title: '操作日志', icon: 'ri-file-lock-line' },
        },
    ],
};

export default operationLogRouter;
