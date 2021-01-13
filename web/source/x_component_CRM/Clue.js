MWF.xApplication.CRM = MWF.xApplication.CRM || {};

MWF.xDesktop.requireApp("Template", "MForm", null, false);
MWF.xDesktop.requireApp("CRM", "Template", null,false);

MWF.require("MWF.widget.Identity", null,false);

MWF.xApplication.CRM.Clue = new Class({
    Extends: MWF.widget.Common,
    Implements: [Options, Events],
    options: {
        "style": "default"
    },

    initialize: function (node, app, actions, options) {
        this.setOptions(options);
        this.app = app;
        this.lp = this.app.lp.clue;
        this.path = "/x_component_CRM/$Clue/";
        this.loadCss();
        this.actions = actions;
        this.node = $(node);
        this.ddown = null;
    },
    loadCss: function () {
        this.cssPath = "/x_component_CRM/$Clue/" + this.options.style + "/css.wcss";
        this._loadCss();
    },
    load: function () {
        if(this.formContentArr)this.formContentArr.empty();
        this.formContentArr = [];
        if(this.formMarkArr)this.formMarkArr.empty();
        this.formMarkArr = [];
        this.rightContentDiv = this.app.rightContentDiv;
        this.createHeadContent();
        //this.createToolBarContent();
        this.createClueContent();
        this.loadEvent();
        this.resizeWindow();
        this.app.addEvent("resize", function(){
            this.resizeWindow();
        }.bind(this));
        jQuery("body").unbind("click");

    },

    reload:function(){
        this.createClueContent();
        this.resizeWindow();
    },
    loadEvent: function() {
        /*that = this;
        debugger
        jQuery(jQuery("body")[0].getElement("#inputleads")).find('.el-dropdown-menu__item').click(function(){
            debugger
            if(jQuery(this).text()=="导入"){
                that.inputObject();
            }
            if(jQuery(this).text()=="导出"){
            }
        });*/
    },
    createHeadContent:function(){
        that = this;
        if(this.headContentDiv) this.headContentDiv.destroy();
        this.headContentDiv = new Element("div.headContentDiv",{"styles":this.css.headContentDiv}).inject(this.rightContentDiv);
        this.headTitleDiv = new Element("div.headTitleDiv",{
            "styles":this.css.headTitleDiv,
            "text":this.lp.head.headTitle
        }).inject(this.headContentDiv);

        //search
        this.headSearchDiv = new Element("div.headSearchDiv",{"styles":this.css.headSearchDiv}).inject(this.headContentDiv);
        this.headSearchTextDiv = new Element("div.headSearchTextDiv",{"styles":this.css.headSearchTextDiv}).inject(this.headSearchDiv);
        this.headSearchImg = new Element("img.headSearchImg",{
            "styles":this.css.headSearchImg,
            "src": this.path+"default/icons/search.png"
        }).inject(this.headSearchTextDiv);
        this.headSearchInput = new Element("input.headSearchInput",{
            "styles":this.css.headSearchInput,
            "placeholder":this.lp.head.searchText
        }).inject(this.headSearchTextDiv);
        this.headSearchInput.addEvents({
            "keyup":function(){
                if(this.headSearchInput.get("value")!=""){
                    this.headSearchRemoveImg.setStyles({"display":"inline-block"})
                }
            }.bind(this)
        });
        this.headSearchRemoveImg = new Element("img.headSearchRemoveImg",{
            "styles":this.css.headSearchRemoveImg,
            "src": this.path+"default/icons/remove.png"
        }).inject(this.headSearchTextDiv);
        this.headSearchRemoveImg.addEvents({
            "click":function(){
                this.headSearchInput.set("value","")
            }.bind(this)
        });
        this.headSearchBottonDiv = new Element("div.headSearchBottonDiv",{
            "styles":this.css.headSearchBottonDiv,
            "text":this.lp.head.search
        }).inject(this.headSearchDiv);
        this.headBottonDiv = new Element("div.headBottonDiv",{"styles":this.css.headBottonDiv}).inject(this.headContentDiv);
        this.headNewBottonDiv = new Element("div.headNewBottonDiv",{
            "styles":this.css.headNewBottonDiv,
            "text" :this.lp.head.create
        }).inject(this.headBottonDiv);
        this.headNewBottonDiv.addEvents({
            "click":function(){
                MWF.xDesktop.requireApp("CRM", "ClueEdit", function(){
                    this.explorer = new MWF.xApplication.CRM.ClueEdit(this, this.actions,{},{
                        "isEdited":true,
                        "isNew":true,
                        "onReloadView" : function(  ){
                            //alert(JSON.stringify(data))
                            this.reload();
                        }.bind(this)
                    });
                    this.explorer.load();
                }.bind(this))
            }.bind(this)

        });
        this.headMoreBottonDiv = new Element("div.headMoreBottonDiv",{
            "styles":this.css.headMoreBottonDiv,
            "text" :this.lp.head.moreAction
        }).inject(this.headBottonDiv);
        this.headMoreImg = new Element("img.headMoreImg",{
            "styles": this.css.headMoreImg,
            "src" : this.path+"default/icons/arrow.png"
        }).inject(this.headMoreBottonDiv);

        this.headMoreConentDiv = new Element("ul.el-dropdown-type",{
            "styles": {"display": "none","width":"105px"},
            "id" :"inputleads"
        }).inject(this.rightContentDiv);
        this.headInputLi = new Element("li.el-dropdown-menu__item",{
            "styles":{"font-size":"13px"},
            "text" :"导入"
        }).inject(this.headMoreConentDiv);
        /*this.headOutputLi = new Element("li.el-dropdown-menu__item",{
            "styles":{"font-size":"13px"},
            "text" :"导出"
        }).inject(this.headMoreConentDiv);*/
        this.arrowDiv = new Element("div.popper__arrow",{
            "styles": {"box-sizing": "border-box !important","border-bottom-style":"none"}
        }).inject(this.headMoreConentDiv);
        this.ddown = jQuery("body")[0].getElement("#inputleads");
        if(this.ddown) this.ddown.setStyles({"top":(jQuery(".headMoreBottonDiv").offset().top+30)+"px","left":(jQuery(".headMoreBottonDiv").offset().left-49)+"px"});
        this.headMoreBottonDiv.addEvents({
            "click":function(){
                jQuery(this.ddown).toggle(100);
            }.bind(this)
        });
        input = this;
        jQuery(this.ddown).find('.el-dropdown-menu__item').click(function(){
            debugger
            if(jQuery(this).text()=="导入"){
                debugger
                input.inputObject();
            }
            if(jQuery(this).text()=="导出"){
            }
        });
    },
    inputObject:function(){
        that = this;
        var contentHtml = '<div  class="vux-flexbox handle-item vux-flex-row" style="align-items: stretch;padding: 10px 20px 0px 10px;line-height:20px;font-size:13px;">'+
            '<div class="handle-item-name" style="margin-top: 8px;width:30px;">一、</div><div style="margin-top: 8px;width:460px;">请按照数据模板的格式准备要导入的数据。点击下载<a href="/x_component_CRM/%E7%BA%BF%E7%B4%A2%E5%AF%BC%E5%85%A5%E6%A8%A1%E6%9D%BF.xlsx">《线索导入模板》</a></div></div>' +
            '<div  class="vux-flexbox handle-item vux-flex-row" style="align-items: stretch;padding: 0px 20px 0px 10px;line-height:20px;font-size:13px;">'+
            '<div class="handle-item-name" style="margin-top: 8px;width:30px;"> </div><div style="margin-top: 8px;width:460px;font-size:12px;color:#a9a9a9;line-height:10px;">注意事项：<p>1、模板中的表头名称不能更改，表头行不能删除</p><p>2、其中标*为必填项，必须填写</p></div></div>' +
            '<div  class="vux-flexbox handle-item vux-flex-row" style="align-items: stretch;padding: 0px 20px 0px 10px;line-height:20px;font-size:13px;">'+
            '<div class="handle-item-name" style="margin-top: 8px;width:30px;">二、</div><div style="margin-top: 8px;;width:460px;">请选择需要导入的文件</div></div>' +
            '<div  class="vux-flexbox handle-item vux-flex-row" style="align-items: stretch;padding: 0px 20px 30px 10px;line-height:20px;font-size:13px;">'+
            '<div class="handle-item-name" style="margin-top: 8px;width:30px;"> </div><div style="margin-top: 8px;;width:460px;">' +
            '<div class="el-input is-disabled" style="display: inline-block;padding-right: 20px;"><input type="text" disabled="disabled" autocomplete="off" class="el-input__inner" style="width: 270px;min-height:25px;" /></div>'+
            '<div style="display: inline-block;color: #fff;background-color: #3e84e9;border-color: #3e84e9;padding: 5px 10px;"><input type="file" id="bar-file" accept="*.*" multiple="multiple" class="bar-input" style="position: absolute;height: 30px;width: 72px;opacity: 0;font-size: 0;cursor: pointer;" /><div><div>选择文件</div></div>'+
            '</div></div>' +
            '</div>'
        Showbo.Msg.confirm('导入线索',contentHtml,function(){
            that.inputData();
        },function(){
            jQuery(that.ddown).toggle(100);
        },550);
        jQuery("#dvMsgCT").find('.bar-input').change(function(event) {
            var files = event.target.files;
            if (files && files.length > 0) {
                // 获取目前上传的文件
                var file = files[0];
                jQuery("#dvMsgCT").find('.el-input__inner').val(file.name);
            }
        });
    },
    inputData:function(){
        that = this;
        var objFile = jQuery("#dvMsgCT").find('#bar-file')[0].files;
        if (objFile && objFile.length > 0) {
            var file = objFile[0];
            var filter = {};
            filter = {
                file:file,
                fileName:file.name
            };
            var formdata=new FormData();
            formdata.append("fileName",file.name);
            formdata.append("file",file);
            debugger
            that.actions.inputLeads(formdata,file,function (attjson) {
                if(attjson.type=="success"){
                    //Showbo.Msg.alert('导入成功!');
                    Showbo.Msg.alert('导入成功!',jQuery("#clue").click());
                }
                jQuery(that.ddown).toggle(100);
            }.bind(that));
        }else{
            jQuery(that.ddown).toggle(100);
        }

    },
    createToolBarContent:function(){

    },
    createClueContent:function(){
        if(this.contentListDiv) this.contentListDiv.destroy();
        this.contentListDiv = new Element("div.contentListDiv",{"styles":this.css.contentListDiv}).inject(this.rightContentDiv);
        if(this.contentListInDiv) this.contentListInDiv.destroy();
        this.contentListInDiv = new Element("div.contentListInDiv",{"styles":this.css.contentListInDiv}).inject(this.contentListDiv);

        var size = this.rightContentDiv.getSize();
        if(this.contentListDiv)this.contentListDiv.setStyles({"height":(size.y-this.headContentDiv.getHeight()-8)+"px"});
        if(this.contentListInDiv)this.contentListInDiv.setStyles({"height":this.contentListDiv.getHeight()+"px","width":"100%"});

        if(this.clueView) delete this.clueView;
        var templateUrl = this.path+"clueView.json";
        var filter = {};
        ////this.customerView =  new  MWF.xApplication.CRM.Customer.View(this.contentListInDiv, this.app, {lp : this.app.lp.curtomerView, css : this.css, actions : this.actions }, { templateUrl : templateUrl,filterData:filter} );
        this.clueView =  new  MWF.xApplication.CRM.Clue.View(
            this.contentListInDiv,
            this.openDiv,
            this.app,
            this,
            { templateUrl : templateUrl,filterData:filter},
            {
                lp:this.app.lp.clueView,
                isAdmin:this.options.isAdmin
            }
        );
        this.clueView.load();
        //this.app.setScrollBar(this.contentListInDiv.getElement(".contentTableNode"),this.customerView,"crm");
    },

    resizeWindow:function(){
        var size = this.rightContentDiv.getSize();
        var rSize = this.headTitleDiv.getSize();
        var lSize = this.headBottonDiv.getSize();
        if(this.headSearchDiv){
            var x = this.headSearchDiv.getSize().x;
            this.headSearchDiv.setStyles({"margin-left":(size.x-rSize.x-lSize.x)/2-(x/2)+"px"});
        }
        //alert(JSON.stringify(size))
        if(this.contentListDiv)this.contentListDiv.setStyles({"height":(size.y-this.headContentDiv.getHeight()-8)+"px"});
        if(this.contentListInDiv)this.contentListInDiv.setStyles({"height":this.contentListDiv.getHeight()+"px"});
    }
});


MWF.xApplication.CRM.Clue.View = new Class({
    Extends: MWF.xApplication.CRM.Template.ComplexView,

    _createDocument: function(data){
        return new MWF.xApplication.CRM.Clue.Document(this.viewNode, data, this.explorer, this);
    },

    _getCurrentPageData: function(callback, count, page, searchText,searchType){
        var category = this.category = this.options.category;
        if (!count)count = 15;
        if (!page)page = 1;
        var id = (this.items.length) ? this.items[this.items.length - 1].data.id : "(0)";

        var filter = this.options.filterData || {};
        /*if(searchText && searchText.trim()!=""){
            filter = {
                key:searchText
            };
        }*/
        filter={key: searchText?searchText.trim():"",
                orderFieldName: "updateTime",
                orderType: "desc"
        };
        debugger
        if (!searchType)searchType = "我负责的线索";
        debugger
        if(searchType=="下属负责的线索"){
            this.actions.ListNestedSubPerson(page, count, filter, function (json) {
                if (callback)callback(json);
            }.bind(this));
        }
        if(searchType=="我负责的线索"){
            this.actions.ListMyDuty(page, count, filter, function (json) {
                if (callback)callback(json);
            }.bind(this));
        }
        if(searchType=="已转化的线索"){
            this.actions.ListTransfer(page, count, filter, function (json) {
                if (callback)callback(json);
            }.bind(this));
        }
        if(searchType=="全部线索"){
            if(this.isAdmin){
                this.actions.getClueListPage(page, count, filter, function (json) {
                    if (callback)callback(json);
                }.bind(this));
            }else{
                this.actions.ListAllMy(page, count, filter, function (json) {
                    debugger
                    if (callback)callback(json);
                }.bind(this));
            }

        }


    },
    _create: function(){

    },
    _openDocument: function(clueId ,clueName){
        /*MWF.xDesktop.requireApp("CRM", "ClueEdit", function(){
            this.explorer = new MWF.xApplication.CRM.ClueEdit(this, this.actions,{},{
                "clueId":clueId,
                "onReloadView" : function(  ){
                    //alert(JSON.stringify(data))
                    this.reload();
                }.bind(this)
            });
            this.explorer.load();
        }.bind(this))*/
        MWF.xDesktop.requireApp("CRM", "ClueOpen", function(){
            this.explorer = new MWF.xApplication.CRM.ClueOpen(this, this.actions,{},{
                "clueId":clueId,
                "clueName":clueName,
                "openType":"single",
                "onReloadView" : function(  ){
                    //alert(JSON.stringify(data))
                    this.reload();
                }.bind(this)
            });
            this.explorer.load();
        }.bind(this))
    },
    _queryCreateViewNode: function(){

    },
    _postCreateViewNode: function( viewNode ){

    },
    _queryCreateViewHead:function(){

    },
    _postCreateViewHead: function( headNode ){

    }

});

MWF.xApplication.CRM.Clue.Document = new Class({
    /*Extends: MWF.xApplication.CRM.Template.ComplexDocument,

    "viewActionReturn":function(){
        return false
    }*/


});