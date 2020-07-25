(function () {
    var root = (typeof self == 'object' && self.self == self && self) ||
        (typeof global == 'object' && global.global == global && global) ||
        this || {};

    // 修复 bind 函数
    Function.prototype.bind = Function.prototype.bind || function (context) {

        if (typeof this !== "function") {
            throw new Error("Function.prototype.bind - what is trying to be bound is not callable");
        }

        var self = this;
        var args = Array.prototype.slice.call(arguments, 1);

        var fNOP = function () {
        };

        var fBound = function () {
            var bindArgs = Array.prototype.slice.call(arguments);
            self.apply(this instanceof fNOP ? this : context, args.concat(bindArgs));
        }

        fNOP.prototype = this.prototype;
        fBound.prototype = new fNOP();
        return fBound;
    }

    var util = {
        extend: function (target) {
            for (var i = 1, len = arguments.length; i < len; i++) {
                for (var prop in arguments[i]) {
                    if (arguments[i].hasOwnProperty(prop)) {
                        target[prop] = arguments[i][prop]
                    }
                }
            }

            return target
        },
        isValidListener: function (listener) {
            if (typeof listener === 'function') {
                return true
            } else if (listener && typeof listener === 'object') {
                return isValidListener(listener.listener)
            } else {
                return false
            }
        },
        indexOf: function (array, item) {
            if (array.indexOf) {
                return array.indexOf(item);
            } else {
                var result = -1;
                for (var i = 0, len = array.length; i < len; i++) {
                    if (array[i] === item) {
                        result = i;
                        break;
                    }
                }
                return result;
            }
        }
    };

    function EventEmitter() {
        this.__events = {}
    }

    EventEmitter.prototype.on = function (eventName, listener) {
        if (!eventName || !listener) return;

        if (!util.isValidListener(listener)) {
            throw new TypeError('listener must be a function');
        }

        var events = this.__events;
        var listeners = events[eventName] = events[eventName] || [];
        var listenerIsWrapped = typeof listener === 'object';

        // 不重复添加事件
        if (util.indexOf(listeners, listener) === -1) {
            listeners.push(listenerIsWrapped ? listener : {
                listener: listener,
                once: false
            });
        }

        return this;
    };

    EventEmitter.prototype.once = function (eventName, listener) {
        return this.on(eventName, {
            listener: listener,
            once: true
        })
    };

    EventEmitter.prototype.off = function (eventName, listener) {
        var listeners = this.__events[eventName];
        if (!listeners) return;

        var index;
        for (var i = 0, len = listeners.length; i < len; i++) {
            if (listeners[i] && listeners[i].listener === listener) {
                index = i;
                break;
            }
        }

        if (typeof index !== 'undefined') {
            listeners.splice(index, 1, null)
        }

        return this;
    };

    EventEmitter.prototype.emit = function (eventName, args) {
        var listeners = this.__events[eventName];
        if (!listeners) return;

        for (var i = 0; i < listeners.length; i++) {
            var listener = listeners[i];
            if (listener) {
                listener.listener.apply(this, args || []);
                if (listener.once) {
                    this.off(eventName, listener.listener)
                }
            }

        }

        return this;

    };

    // 惰性函数 addEvent
    var addEvent = (function () {
        if (window.addEventListener) {
            return function (elem, type, fn) {
                elem.addEventListener(type, fn, false);
            }
        } else if (window.attachEvent) {
            return function (elem, type, fn) {
                elem.attachEvent('on' + type, fn);
            }
        }
    })();

    function WaterFall(opts) {

        EventEmitter.call(this);

        this.opts = util.extend({}, this.constructor.defaultopts, opts);

        this._container = typeof this.opts.container === 'string' ? document.querySelector(this.opts.container) : this.opts.container;
        this._pins = typeof this.opts.pins === 'string' ? document.querySelectorAll(this.opts.pins) : this.opts.pins;
        this._loader = typeof this.opts.loader === 'string' ? document.querySelector(this.opts.loader) : this.opts.loader;

        this.init();
    }

    WaterFall.VERSION = '1.0.0';

    WaterFall.defaultopts = {
        gapHeight: 20,
        gapWidth: 20,
        pinWidth: 216,
        threshold: 100
    }

    var proto = WaterFall.prototype = new EventEmitter();

    proto.constructor = WaterFall;

    proto.init = function () {

        // 计算有多少列
        this.getColumnNum();
        // 设置 container 居中
        this.setContainer();

        // 如果已经有图片，设置为瀑布流
        if (this._pins.length > 0) {
            this.setPosition(this._pins)
        }

        var self = this;
        // 设置瀑布流
        setTimeout(function () {
            self.setWaterFall();
        }, 0)
        // 绑定滚动事件
        this.bindScrollEvent();

    };

    proto.getColumnNum = function () {
        this._unitWidth = this.opts.pinWidth + this.opts.gapWidth;

        this._viewPortWidth = window.innerWidth || document.documentElement.clientWidth;
        this._viewPortHeight = window.innerHeight || document.documentElement.clientHeight;

        this._num = Math.floor((this._viewPortWidth + this.opts.gapWidth) / this._unitWidth);

        // 用于储存每列的高度，起始都为 0
        this._columnHeightArr = [];
        for (var i = 0; i < this._num; i++) {
            this._columnHeightArr[i] = 0;
            ;
        }
    };

    /**
     * 计算并且设置 container 宽度，使其居中
     */
    proto.setContainer = function () {
        this._container.style.width = (this._unitWidth * this._num - this.opts.gapWidth) + 'px';
    };

    /**
     * 获取高度数组中的最小值，用于确定下个 pin 插入到那一列中
     */
    proto.getMin = function () {
        return Math.min.apply(null, this._columnHeightArr);
    };

    /**
     * 获取高度数组中的最大值，用于设置 loading 的 top 值
     */
    proto.getMax = function () {
        return Math.max.apply(null, this._columnHeightArr);
    };

    // 保证一次只进行一次加载
    var load = false;

    proto.appendPins = function () {
        if (load) return;

        load = true;

        // 显示 loading
        if (this._loader) {
            this._loader.style.display = 'block';
            this._loader.style.top = (this.getMax() + 100) + 'px';
            this._loader.style.left = '50%';
            this._loader.style.height = '50px';
            this._loader.style.width = '50px';
        }

        // 保证短时间内只触发一次
        this.emit("load");
    };

    proto.append = function (html, selector) {

        this._checkResult = [];
        this._newPins = [];

        var div = document.createElement("div")
        div.innerHTML = html;

        children = div.querySelectorAll(this.opts.pins)

        var fragment = document.createDocumentFragment();

        for (var j = 0, len = children.length; j < len; j++) {
            fragment.appendChild(children[j])
            this._checkResult[j] = false;
            this._newPins.push(children[j])
            this._checkImgHeight(children[j], selector, j)
        }

        this.isReadyAppend(fragment)
    };

    proto._checkImgHeight = function (childNode, selector, index) {

        var startTime = new Date().getTime();

        var img = childNode.querySelector(selector);

        var self = this;

        // 本地图片会先执行 onload 事件
        img.onload = function () {
            if (img.getAttribute('height')) return;
            // 得到高度后，设置高度
            img.setAttribute('height', Math.floor(img.height / img.width * self.opts.pinWidth));
            // 通过标志量表示该图片已经设置了高度
            self._checkResult[index] = true

            clearInterval(timer)
        }

        img.onerror = function () {

            if (img.getAttribute('height')) return;

            img.setAttribute('height', 250);

            self._checkResult[index] = true

            clearInterval(timer)

        }

        if (img.getAttribute('height')) return img;

        // 通过设置 interval 来最快得到加载中的图片高度
        var check = function () {
            if (img.width > 0 && img.height > 0) {

                img.setAttribute('height', Math.floor(img.height / img.width * self.opts.pinWidth));

                self._checkResult[index] = true

                clearInterval(timer)

            }
        }

        var timer = setInterval(check, 40)

    };

    proto.isReadyAppend = function (fragment) {
        // 只有当所有图片都具有高度的时候，才添加进文档树
        var self = this;

        var checkAllHaveHeight = function () {
            if (util.indexOf(self._checkResult, false) == -1) {

                self._container.appendChild(fragment);
                // 可以加载新的数据
                load = false;
                // 隐藏 loading
                if (self._loader) {
                    self._loader.style.display = 'none';
                }
                // 对新添加的 pins 设置位置
                self.setPosition(self._newPins);
                clearTimeout(timer)
            } else {
                setTimeout(checkAllHaveHeight)
            }
        }
        var timer = setTimeout(checkAllHaveHeight, 40);
    };

    /**
     * 设置新的 pins 的位置
     */
    proto.setPosition = function (pins) {

        for (var i = 0, len = pins.length; i < len; i++) {
            var min = this.getMin();
            var index = util.indexOf(this._columnHeightArr, min);

            pins[i].style.left = this._unitWidth * index + 'px';

            pins[i].style.top = min + 'px';

            this._columnHeightArr[index] += (pins[i].offsetHeight + this.opts.gapHeight);
        }

        this._newPins = [];
        this.setWaterFall()

    };

    /**
     * 判断是否需要添加新的 pins，在 init 的时候会调用一次，保证首屏充满
     */
    proto.setWaterFall = function () {
        if (this.getMin() < this._viewPortHeight) {
            this.appendPins();
        }
    };

    proto.bindScrollEvent = function () {
        addEvent(window, "scroll", this.handleScroll.bind(this));
        addEvent(window, "resize", this.handleResize.bind(this))
    };

    var timer = null;

    proto.handleResize = function () {
        var self = this;
        clearTimeout(timer);
        timer = setTimeout(function () {
            self.resetPosition()
        }, 100)
    };

    proto.resetPosition = function () {
        // 计算有多少列
        this.getColumnNum();
        // 设置 container 居中
        this.setContainer();
        // 设置瀑布流
        this.setPosition(typeof this.opts.pins === 'string' ? document.querySelectorAll(this.opts.pins) : this.opts.pins);
    };

    /**
     * 只要有空白处，就可以加载新的数据
     */
    proto.checkScroll = function () {
        if (this.getMin() - (window.pageYOffset || document.documentElement.scrollTop) < this._viewPortHeight + this.opts.threshold) {
            return true
        }
        return false;
    };

    proto.handleScroll = function () {

        var self = this;
        //console.log(self.checkScroll())
        if (self.checkScroll()) {

            self.appendPins();
        }
    };

    if (typeof exports != 'undefined' && !exports.nodeType) {
        if (typeof module != 'undefined' && !module.nodeType && module.exports) {
            exports = module.exports = WaterFall;
        }
        exports.WaterFall = WaterFall;
    } else {
        root.WaterFall = WaterFall;
    }
    //开源不易，建议保留作者版权信息，感谢支持。
    console.log("\n %c 高清壁纸站 %c by Hellohao博客 | www.hellohao.cn", "color:#444;background:#eee;padding:5px 0;", "color:#eee;background:#444;padding:5px 0;");
}());