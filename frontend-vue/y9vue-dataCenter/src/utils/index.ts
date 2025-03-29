/**
 * 判断是否是移动端
 * @returns {Boolean}
 * @author Yehaifeng
 */
export const isMobile = (): Boolean => {
    return /Android|webOS| iPhone | iPad | iPod |BlackBerry|opera mini|opera mobile|appleWebkit.*mobile|mobile/i.test(
        navigator.userAgent
    );
};

/**
 * 根据key（必须为数字）值排序
 * 使用示例 array.sort(compare("key"))
 * @returns {Array}
 * @author Yehaifeng
 */
export const compare = function (key) {
    return function (obj1, obj2) {
        let val1 = obj1[key];
        let val2 = obj2[key];
        if (!isNaN(Number(val1)) && !isNaN(Number(val2))) {
            val1 = Number(val1);
            val2 = Number(val2);
        }
        if (val1 < val2) {
            return -1;
        } else if (val1 > val2) {
            return 1;
        } else {
            return 0;
        }
    };
};

/**
 * 随机生成字符串
 * 使用示例 randomString(6) 生成6位的字符串
 * @returns {String}
 * @author Yehaifeng
 */
export const randomString = (e) => {
    var e = e || 32,
        t = 'ABCDEFGHJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789',
        a = t.length,
        n = '';
    for (let i = 0; i < e; i++) n += t.charAt(Math.floor(Math.random() * a));
    return n;
};

/**
 * 防抖函数
 * 使用示例 debounce(fun,wait)  fun:事件处理函数， wait:延迟时间
 * @returns {String}
 * @author Yehaifeng
 */
export const debounce = (fun: Function, wait: number): Function => {
    var timer; //维护全局纯净，借助闭包来实现
    return function () {
        if (timer) {
            //timer有值为真，这个事件已经触发了一次，重新开始计数
            clearTimeout(timer);
        }
        timer = setTimeout(function () {
            fun();
        }, wait);
    };
};

/**
 * 生成guid函数
 * 使用示例 uuid("xxxx-yyyy-xx-yy")
 * @params guidFormat 传入guid字符串的输出模版 传入 xx-yy 输出 e0-6k
 * @returns {String}
 * @author Yehaifeng
 */
function uuid(guidFormat = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx') {
    let guid = guidFormat.replace(/[xy]/g, function (c) {
        var r = (Math.random() * 16) | 0,
            v = c == 'x' ? r : (r & 0x3) | 0x8;
        return v.toString(16);
    });
    return guid;
}

//判断时间
export function getDay(day) {
    var today = new Date();
    var targetday_milliseconds = today.getTime() + 1000 * 60 * 60 * 24 * day;
    today.setTime(targetday_milliseconds); //注意，这行是关键代码
    var tYear = today.getFullYear();
    var tMonth = today.getMonth();
    var tDate = today.getDate();
    tMonth = doHandleMonth(tMonth + 1);
    tDate = doHandleMonth(tDate);
    //return tYear+"-"+tMonth+"-"+tDate;
    return tYear + '-' + tMonth;
}
function doHandleMonth(month) {
    var m = month;
    if (month.toString().length == 1) {
        m = '0' + month;
    }
    return m;
}

/***
 * 遍历返回数据，屏蔽高朋|高市长
 */
export function replaceGoodWords(data) {
    if (Array.isArray(data)) {
        // 如果是数组，递归处理每个元素
        return data.map(replaceGoodWords);
    } else if (typeof data === 'object' && data !== null) {
        // 如果是对象，递归处理每个属性及属性值
        const newData = {};
        for (const key in data) {
            if (data.hasOwnProperty(key)) {
                // 属性
                var keyValue = replaceGoodWords(key);
                // 属性值
                newData[keyValue] = replaceGoodWords(data[key]);
            }
        }
        return newData;
    } else if (typeof data === 'string') {
        // 如果是字符串，替换 '高朋', '高市长' 为空字符串
        return data.replace(/(高朋|高市长)/g, '');
    } else {
        // 其他类型的数据直接返回
        return data;
    }
}
