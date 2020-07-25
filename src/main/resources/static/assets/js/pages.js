;(function($){
    var falless=(function(){
        function falless(obj,config){
            this.opts=$.extend(true, $.fn.falless.config, config);
            this.win=obj;
            this._init_();
        }
        return falless;
    })();
    falless.prototype={
        _init_:function(){
            var _this_=this;
            this.gird=this.win.find(this.opts.fallElement)
            this.fallAct();
        },
        fallAct:function(){
            var _this_=this;
            var gird=this.gird;
            var col=this.column();
            var colarr=[];
            this.win.width(gird.outerWidth()*col).css({"margin":"0 auto","padding-right":this.opts.offsetX+"px"})
            gird.each(function(index, el) {
                var h=gird.eq(index).height()
                if(index < col){
                    colarr.push(h)
                    $(el).removeClass('pa')
                }else{
                    var minh=Math.min.apply(null,colarr);
                    var minIndex=$.inArray(minh,colarr);
                    $(el).addClass("pa").css({
                        'top': (minh + _this_.opts.offsetY)+"px",
                        'left':(gird.eq(minIndex).position().left)+"px"
                    });
                    colarr[minIndex]+=$(el).height()+_this_.opts.offsetY;
                    _this_.win.height(Math.max.apply(null,colarr))
                    _this_.colarr=colarr;
                }
            });
        },
        column:function(){
            var winW=$(window).width();
            var girdWidth=this.gird.outerWidth();
            if(this.opts.col=="auto"){
                var col=Math.floor(winW/girdWidth)
            }else if($.type(this.opts.col*1)=="number"){
                col=this.opts.col
            }
            return col;
        }
    };
    $.fn.falless=function(config){
        return this.each(function(){
            var _this_=$(this);
            var obj= new falless(_this_,config)
            $(window).resize(function(event) {
                var obj= new falless(_this_,config)
            });
        })
    };

    $.fn.falless.config={
        col:'auto',
        offsetX:10,
        offsetY:10,
        fallElement:'.gird',
    };
    window.falless=falless ;
})(jQuery)
