
window.onload = function(){
	$('#loading-mask').fadeOut();
}
var onlyOpenTitle="欢迎使用";//不允许关闭的标签的标题

var _menus={
		"icon":"icon-sys",
		"menuid":"0",
		"menuname":"系统菜单",
		"menus":
			[
			 	{
			 		"icon":"icon-sys","menuid":"100","menuname":"一级菜单","menus":
					[
						{"icon":"icon-sys","menuid":"101","menuname":"二级菜单","url":""}	,
						{"icon":"icon-sys","menuid":"102","menuname":"二级菜单","url":""}						
					]
			 	}
			 	
			 ]
		};




$(function(){	
	
	showName();
	getMenuTree();
	tabClose();
	tabCloseEven();
	
	
})

$("#loginOut").click(function() {
	$.post('login_logOut.action', function () {
		location.href="login.html";
	})
})

// 显示登录名
function showName() {
	$.ajax({
		type: 'post',
		url: 'login_showName.action',
		dataType: 'json',
		success: function (res) {
			if (res.success) {
				$("#username").html(res.message);
			} else {
				location.href="login.html";
			}
		}
	})
}

function getMenuTree() {
	$.ajax({
		type: 'post',
		url: 'menu_getMenuTree.action',
		dataType: 'json',
		success: function(res) {
			_menus = res;
			InitLeftMenu();
		}
	})
}

//初始化左侧
function InitLeftMenu() {
	$("#nav").accordion({animate:false,fit:true,border:false});
	var selectedPanelname = '';
	
	    $.each(_menus.menus, function(i, n) {
			var menulist ='';
			menulist +='<ul class="navlist">';
	        $.each(n.menus, function(j, o) {
				menulist += '<li><div ><a ref="'+o.menuid+'" href="#" rel="' + o.url + '" ><span class="icon '+o.icon+'" >&nbsp;</span><span class="nav">' + o.menuname + '</span></a></div> ';
				/*
				if(o.child && o.child.length>0)
				{
					//li.find('div').addClass('icon-arrow');
	
					menulist += '<ul class="third_ul">';
					$.each(o.child,function(k,p){
						menulist += '<li><div><a ref="'+p.menuid+'" href="#" rel="' + p.url + '" ><span class="icon '+p.icon+'" >&nbsp;</span><span class="nav">' + p.menuname + '</span></a></div> </li>'
					});
					menulist += '</ul>';
				}
				*/
				menulist+='</li>';
	        })
			menulist += '</ul>';
	
			$('#nav').accordion('add', {
	            title: n.menuname,
	            content: menulist,
					border:false,
	            iconCls: 'icon ' + n.icon
	        });
	
			if(i==0)
				selectedPanelname =n.menuname;
	
	    });
	
	$('#nav').accordion('select',selectedPanelname);



	$('.navlist li a').click(function(){
		var tabTitle = $(this).children('.nav').text();

		var url = $(this).attr("rel");
		var menuid = $(this).attr("ref");
		var icon = $(this).find('.icon').attr('class');

		var third = find(menuid);
		if(third && third.child && third.child.length>0)
		{
			$('.third_ul').slideUp();

			var ul =$(this).parent().next();
			if(ul.is(":hidden"))
				ul.slideDown();
			else
				ul.slideUp();



		}
		else{
			addTab(tabTitle,url,icon);
			$('.navlist li div').removeClass("selected");
			$(this).parent().addClass("selected");
		}
	}).hover(function(){
		$(this).parent().addClass("hover");
	},function(){
		$(this).parent().removeClass("hover");
	});





	//选中第一个
	//var panels = $('#nav').accordion('panels');
	//var t = panels[0].panel('options').title;
    //$('#nav').accordion('select', t);
}
//获取左侧导航的图标
function getIcon(menuid){
	var icon = 'icon ';
	$.each(_menus.menus, function(i, n) {
		 $.each(n.menus, function(j, o) {
		 	if(o.menuid==menuid){
				icon += o.icon;
			}
		 })
	})

	return icon;
}

function find(menuid){
	var obj=null;
	$.each(_menus.menus, function(i, n) {
		 $.each(n.menus, function(j, o) {
		 	if(o.menuid==menuid){
				obj = o;
			}
		 });
	});

	return obj;
}

function addTab(subtitle,url,icon){
	if(!$('#tabs').tabs('exists',subtitle)){
		$('#tabs').tabs('add',{
			title:subtitle,
			content:createFrame(url),
			closable:true,
			icon:icon
		});
	}else{
		$('#tabs').tabs('select',subtitle);
		$('#mm-tabupdate').click();
	}
	tabClose();
}

function createFrame(url)
{
	var s = '<iframe scrolling="auto" frameborder="0"  src="'+url+'" style="width:100%;height:100%;"></iframe>';
	return s;
}

function tabClose()
{
	/*双击关闭TAB选项卡*/
	$(".tabs-inner").dblclick(function(){
		var subtitle = $(this).children(".tabs-closable").text();
		$('#tabs').tabs('close',subtitle);
	})
	/*为选项卡绑定右键*/
	$(".tabs-inner").bind('contextmenu',function(e){
		$('#mm').menu('show', {
			left: e.pageX,
			top: e.pageY
		});

		var subtitle =$(this).children(".tabs-closable").text();

		$('#mm').data("currtab",subtitle);
		$('#tabs').tabs('select',subtitle);
		return false;
	});
}


//绑定右键菜单事件
function tabCloseEven() {

    $('#mm').menu({
        onClick: function (item) {
            closeTab(item.id);
        }
    });

    return false;
}

function closeTab(action)
{
    var alltabs = $('#tabs').tabs('tabs');
    var currentTab =$('#tabs').tabs('getSelected');
	var allTabtitle = [];
	$.each(alltabs,function(i,n){
		allTabtitle.push($(n).panel('options').title);
	})


    switch (action) {
        case "refresh":
            var iframe = $(currentTab.panel('options').content);
            var src = iframe.attr('src');
            $('#tabs').tabs('update', {
                tab: currentTab,
                options: {
                    content: createFrame(src)
                }
            })
            break;
        case "close":
            var currtab_title = currentTab.panel('options').title;
            $('#tabs').tabs('close', currtab_title);
            break;
        case "closeall":
            $.each(allTabtitle, function (i, n) {
                if (n != onlyOpenTitle){
                    $('#tabs').tabs('close', n);
				}
            });
            break;
        case "closeother":
            var currtab_title = currentTab.panel('options').title;
            $.each(allTabtitle, function (i, n) {
                if (n != currtab_title && n != onlyOpenTitle)
				{
                    $('#tabs').tabs('close', n);
				}
            });
            break;
        case "closeright":
            var tabIndex = $('#tabs').tabs('getTabIndex', currentTab);

            if (tabIndex == alltabs.length - 1){
                alert('亲，后边没有啦 ^@^!!');
                return false;
            }
            $.each(allTabtitle, function (i, n) {
                if (i > tabIndex) {
                    if (n != onlyOpenTitle){
                        $('#tabs').tabs('close', n);
					}
                }
            });

            break;
        case "closeleft":
            var tabIndex = $('#tabs').tabs('getTabIndex', currentTab);
            if (tabIndex == 1) {
                alert('亲，前边那个上头有人，咱惹不起哦。 ^@^!!');
                return false;
            }
            $.each(allTabtitle, function (i, n) {
                if (i < tabIndex) {
                    if (n != onlyOpenTitle){
                        $('#tabs').tabs('close', n);
					}
                }
            });

            break;
        case "exit":
            $('#closeMenu').menu('hide');
            break;
    }
}


//弹出信息窗口 title:标题 msgString:提示信息 msgType:信息类型 [error,info,question,warning]
function msgShow(title, msgString, msgType) {
	$.messager.alert(title, msgString, msgType);
}




//设置登录窗口
function openPwd() {
    $('#w').window({
        title: '修改密码',
        width: 300,
        modal: true,
        shadow: true,
        closed: true,
        height: 160,
        resizable:false,
        buttons:[
        	{text:'确定',iconCls:'icon-save',handler:updatePwd},
        	{text:'取消',iconCls:'icon-cancel',handler:closePwd}
        ]
    });
}
function updatePwd() {
	var $newpass = $("#txtNewPass");
	var $oldpass = $("#txtOldPass");
	var $repass = $("#txtRePass");
	
	if (!$oldpass.val()) {
		$.messager.alert("提示","请输入旧密码","info",function() {
			$oldpass.select();
		})
		return false;
	}
	if (!$newpass.val()) {
		$.messager.alert("提示","请输入新密码","info",function() {
			$newpass.select();
		})
		return false;
	}
	if (!$repass.val()) {
		$.messager.alert("提示","请确定密码","info",function() {
			$repass.select();
		})
		return false;
	}
	if ($repass.val() != $newpass.val()) {
		$.messager.alert("提示","两次密码不一致","info",function() {
			$repass.select();
		})
		return false;
	}
	
	$.ajax({
		type: 'post',
		url: 'emp_updatePwd.action',
		dataType: 'json',
		data: {newPwd: $newpass.val(), oldPwd: $oldpass.val()},
		success: function (res) {
			$.messager.alert("提示", res.message, "info", function () {
				if (res.success) {
					$newpass.val("");
					$oldpass.val("");
					$repass.val("");
					$("#w").dialog("close");
				}
			});
		}
	})
}

//关闭登录窗口
function closePwd() {
    $('#w').window('close');
}


$(function() {

    openPwd();

    $('#editpass').click(function() {
        $('#w').window('open');
    });

    $('#btnEp').click(function() {
    	updatePwd();
    })

	$('#btnCancel').click(function(){closePwd();})

   
});


