MWF.xApplication.CRM.AddressExplorer = null, MWF.xApplication.CRM.CustomerEdit = new Class({
    Extends: MWF.xApplication.CRM.Template.PopupForm,
    Implements: [Options, Events],
    options: {
        style: "default",
        width: "800",
        height: "100%",
        top: 0,
        left: 0,
        hasTop: !0,
        hasIcon: !1,
        hasBottom: !0,
        title: "",
        draggable: !1,
        closeAction: !0
    },
    initialize: function (e, t, i, l) {
        this.setOptions(l), this.explorer = e, this.app = e.app, this.lp = this.app.lp.customer.customerEdit, this.path = "/x_component_CRM/$CustomerEdit/", this.cssPath = this.path + this.options.style + "/css.wcss", this._loadCss(), this.options.title = this.lp.title, this.data = i || {}, this.province = null,this.actions = t
    },
    load: function () {
        this.loadResource(function () {
            this.appArea = jQuery("body").children(":first"), this.createForm()
        }.bind(this))
    },
    loadResource: function (e) {
        e && e()
    },
    createForm: function () {
        _self = this, jQuery(_self.appArea).next().attr("style", ""), jQuery(_self.appArea).next().attr("class", "mask");
        var o = _self.lp, e = '<div class="section-conent">';
        for (i in o) {
            var t = o[i].type;
            var l = o[i].notEmpty ? o[i].notEmpty : "false";
                if (t =="datetime") {
                    r = '<input type="text" class="inline-input" readonly="readonly" name="' + i + '" id="' + i + '" notEmpty="' + l + '" stype="' + t + '">';
                }else{
                    r = '<input type="text" class="inline-input" name="' + i + '" id="' + i + '" notEmpty="' + l + '" stype="' + t + '">';
                }
            "textarea" == t && (r = '<textarea rows="6" class="el-textarea__inner"  id="' + i + '" notEmpty="' + l + '" stype="' + t + '"  style="resize: none; min-height: 30.6px;"></textarea>'), "select" != t && "hide" != t || (r = '<div class="inline-input" style="display: inline-block;cursor:pointer;"  id="' + i + '" notEmpty="' + l + '" stype="' + t + '" ></div><div class="el-icon-arrow-down el-icon--right" style="margin-left: -20px; display: inline-block;"><img src="/x_component_CRM/$Clue/default/icons/arrow.png"></div>'), "map" == t && (r = '<div class="setMap" id="setMap" stype="' + t + '"></div>'), e = e + '<div class="conent-inline"><div class="conent-title" lable="' + i + '">' + o[i].text + '</div><div class="conent-value">' + r + "</div></div>"
        }
        var s = '<div class="section-header"><div class="section-mark" style="border-left-color: rgb(70, 205, 207);"></div> <div data-v-ec8f8850="" class="section-title">基本信息</div></div>' + (e += "</div>") + '<div class="section_button"><div><button class="el-button handle-button el-button-cancle"><span>取消</span></button><button class="el-button handle-button el-button-primary"><span>保存</span></button></div></div>';
        jQuery(".headMoreImg").notifyMe("left", "default", "新建客户", "", "", s, "notifyEdit", 50), jQuery(".conent-value").each(function (e, t) {
            var i = jQuery(t).children().eq(0), l = jQuery(i).attr("stype");
            if ("datetime" == l && _self.loadTimeContainer(jQuery(i).attr("id")), "select" == l || "hide" == l) {
                var r = _self.app.lp.customer;
                for (j in r) if (j == jQuery(i).attr("id")) {
                    var s = o[j].value.split(",");
                    if (0 < s.length) {
                        for (var n = '<ul class="el-dropdown-type" style="display: none;" tid="' + jQuery(i).attr("id") + '">', a = 0; a < s.length; a++) n = n + '<li class="el-dropdown-menu__item">' + s[a] + "</li>";
                        jQuery(".notify-content").append(n + '<div class="popper__arrow"></div></ul>'), jQuery(i).click(function () {
                            jQuery("[tid='" + jQuery(i).attr("id") + "']").css({
                                left: jQuery(i).offset().left - 50,
                                top: jQuery(i).offset().top + 30,
                                width: 282
                            }), jQuery("[tid='" + jQuery(i).attr("id") + "']").toggle(100)
                        }), jQuery("[tid='" + jQuery(i).attr("id") + "']").children().click(function () {
                            jQuery(i).text(jQuery(this).text()), jQuery("[tid='" + jQuery(i).attr("id") + "']").toggle(100)
                        })
                    }
                }
            }
        }), jQuery(".el-button-cancle").click(function () {
            setTimeout(function () {
                jQuery("#notifyEdit").remove(), 0 < jQuery(".mask").length && (jQuery(".mask").attr("style", "left: 0px; top: 0px; width: 100%; overflow: hidden; position: absolute; z-index: 500000; background-color: rgb(255, 255, 255)"), jQuery(".mask").attr("class", ""))
            }, 200)
        }), jQuery(".el-button-primary").click(function () {
            var l = !0;
            if (jQuery(".inline-input[notempty='true']").each(function (e, t) {
                if ("" == jQuery(t).val() && "" == jQuery(t).text()) {
                    l = !1;
                    var i = jQuery(t).parent().prev().text() + "不能为空";
                    0 < jQuery(t).nextAll(".empError").length && jQuery(t).nextAll(".empError").remove(), jQuery(t).parent().append('<div class="empError" style="color:#f56c6c;padding: 0;line-height: 1;">' + i + "</div>")
                } else 0 < jQuery(t).nextAll(".empError").length && jQuery(t).nextAll(".empError").remove()
            }), l) {
                var e;
                var provinceStr =  jQuery("[name='province']").val()+"#"+jQuery("[name='city']").val()+"#"+jQuery("[name='district']").val();
                e = {
                    customername: jQuery('div[lable="customername"]').next().children().eq(0).val(),
                    level: jQuery('div[lable="level"]').next().children().eq(0).text(),
                    industry: jQuery('div[lable="industry"]').next().children().eq(0).text(),
                    source: jQuery('div[lable="source"]').next().children().eq(0).text(),
                    dealstatus: jQuery('div[lable="dealstatus"]').next().children().eq(0).text(),
                    telephone: jQuery('div[lable="telephone"]').next().children().eq(0).val(),
                    cellphone: jQuery('div[lable="cellphone"]').next().children().eq(0).val(),
                    website: jQuery('div[lable="website"]').next().children().eq(0).val(),
                    location: jQuery('div[lable="detailaddress"]').next().children().eq(0).attr("location"),
                    detailaddress: jQuery('div[lable="detailaddress"]').next().children().eq(0).val(),
                    lng: jQuery('div[lable="detailaddress"]').next().children().eq(0).attr("lng"),
                    lat: jQuery('div[lable="detailaddress"]').next().children().eq(0).attr("lat"),
                    province:provinceStr.indexOf("0")>-1?"":provinceStr,
                    city: jQuery('div[lable="detailaddress"]').next().children().eq(0).attr("city"),
                    nexttime: jQuery('div[lable="nexttime"]').next().children().eq(0).val(),
                    remark: jQuery('div[lable="remark"]').next().children().eq(0).val()
                }, _self.actions.saveCustomer(e, function (e) {
                    "success" == e.type && Showbo.Msg.alert("保存成功!", jQuery("#customer").click()), setTimeout(function () {
                        jQuery("#notifyEdit").remove(), 0 < jQuery(".mask").length && (jQuery(".mask").attr("style", "left: 0px; top: 0px; width: 100%; overflow: hidden; position: absolute; z-index: 500000; background-color: rgb(255, 255, 255)"), jQuery(".mask").attr("class", ""))
                    }, 200)
                }.bind(_self))
            }
        }), jQuery(".notify-content").click(function(e){
            if(jQuery(e.target).closest(".inline-input[notempty='true']").length <1 && jQuery(e.target)[0].innerText!="取消"){
                jQuery(".inline-input[notempty='true']").each(function(index,element){
                    if(jQuery(element).val()=="" && jQuery(element).text()==""){
                        var nameStr = jQuery(element).parent().prev().text()+'不能为空';
                        if(jQuery(element).nextAll(".empError").length>0)jQuery(element).nextAll(".empError").remove();
                        jQuery(element).parent().append('<div class="empError" style="color:#f56c6c;padding: 0;line-height: 1;">'+nameStr+'</div>');
                    }else{
                        if(jQuery(element).nextAll(".empError").length>0)jQuery(element).nextAll(".empError").remove();
                    }
                });

            }
        }),_self.getAddress()
    },
    getAddress: function(){
        var _self= this;
        var province = [];
        o2.Actions.get("x_general_assemble_control").listProvince(function(json){
            json.data.each(function(text){
                province.push(text.name);
            }.bind(this));
            /*if (province.length) if (callback) callback(province);*/
        }.bind(this),null,false);

        var provinceHtml = "";
        for(var i=0;i<province.length;i++){
            provinceHtml = provinceHtml+'<option value="'+province[i]+'">'+province[i]+'</option>'
        }
        var addressHtml = '<select name="province" class="select-address"><option value="0">所在省</option>'+provinceHtml+'</select><select name="city" class="select-address city-address"> <option value="0">所在市</option></select> <select class="select-address" name="district"><option value="0">所在区</option></select>'
        jQuery("#province").parent().append(addressHtml);
        jQuery("#province").remove();

        if(_self.province != null){
            var provinceList = _self.province.split("#");
            if(provinceList.length>1){
                jQuery("[name='province']").append(new Option(provinceList[0],provinceList[0]));
                jQuery("[name='province']").val(provinceList[0]);
                jQuery("[name='city']").append(new Option(provinceList[1],provinceList[1]));
                jQuery("[name='city']").val(provinceList[1]);
                jQuery("[name='district']").append(new Option(provinceList[2],provinceList[2]));
                jQuery("[name='district']").val(provinceList[2]);
            }
        }

        jQuery("[name='province']").change(function(){
            _self.getCity(this.value);
        });
        jQuery("[name='city']").change(function(){
            _self.getDistrict(jQuery("[name='province']").val(),this.value)
        });
    },
    getCity: function(province) {
        jQuery("[name='city']").find("option[value!='0']").remove();
        jQuery("[name='district']").find("option[value!='0']").remove();
        var sltCity=jQuery("[name='city']");
        if(province !="所在省"){
            o2.Actions.get("x_general_assemble_control").listCity(province, function(json){
                json.data.each(function(text){
                    jQuery(sltCity).append(new Option(text.name,text.name));
                });
            }.bind(this),null,false);
        }
    },
    getDistrict: function(province,city){
        jQuery("[name='district']").find("option[value!='0']").remove();
        var sltDistrict=jQuery("[name='district']");
        o2.Actions.get("x_general_assemble_control").listDistrict(province, city, function(json){
            json.data.each(function(text){
                jQuery(sltDistrict).append(new Option(text.name,text.name));
            });
        },null,false);
    },
    loadTimeContainer: function (e) {
        jQuery("#" + e).ymdateplugin({showTimePanel: !0})
    },
    loadMap: function () {
        //var sheight = (screen.height>1400)?"1050px":"700px";
        (_self = this).mapDiv = jQuery("#setMap")[0], jQuery(".section-conent").css("height", (screen.height-400)+"px"), this.mapDiv && this.mapDiv.empty(), this.addressModule && delete this.addressModule, MWF.xDesktop.requireApp("CRM", "AddressExplorer", function () {
            this.addressModule = new MWF.xApplication.CRM.AddressExplorer(this.mapDiv, this, this.actions, {}), this.addressModule.load()
        }.bind(this))
    },
    getItemTemplate: function (e) {
        return _self = this, {
            customername: {
                text: e.customername,
                type: "text",
                notEmpty: !0,
                value: this.customerData && this.customerData.customername ? this.customerData.customername : ""
            },
            level: {type: "select", notEmpty: !0, text: e.level, value: this.app.lp.customer.level.value},
            industry: {type: "select", notEmpty: !0, text: e.industry, value: this.app.lp.customer.industry.value},
            source: {type: "select", notEmpty: !0, text: e.source, value: this.app.lp.customer.source.value},
            telephone: {type: "text", notEmpty: !0, text: e.telephone, value: this.app.lp.clue.level.value},
            website: {text: e.website, type: "text"},
            nexttime: {text: e.nexttime, notEmpty: !0, attr: {id: "nexttime"}, type: "datetime"},
            cellphone: {text: e.cellphone, type: "text"},
            detailaddress: {text: e.detailaddress, type: "text"},
            remark: {text: e.remark, type: "textarea"},
            location: {text: e.location, type: "map"}
        }
    }
});