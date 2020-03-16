layui.use(['form','layer','jquery'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer
        $ = layui.jquery;

    $(".loginBody .seraph").click(function(){
        layer.msg("这只是做个样式，至于功能，你见过哪个后台能这样登录的？还是老老实实的找管理员去注册吧",{
            time:5000
        });
    })

    //登录按钮
    // form.on("submit(authorize)",function(data){
    //     // var btn = $(this);
    //     // btn.text("登录").attr("disabled","disabled").addClass("layui-disabled");
    //     // $.post("/authorize",data.field,function(data){
    //     //     btn.text("登录").attr("disabled",false).removeClass("layui-disabled");
    //     //     alert(JSON.stringify(result));
    //     // },"json");
    //     $.ajax({
    //         url: "/authorize",
    //         type: "POST",
    //         data: data.field,
    //         xhrFields: {
    //             withCredentials: true
    //         },
    //         crossDomain: true,
    //         success: function (res) {
    //             window.location.href = '/index';
    //         }
    //     });
    //     return false;
    // })

    //表单输入效果
    $(".loginBody .input-item").click(function(e){
        e.stopPropagation();
        $(this).addClass("layui-input-focus").find(".layui-input").focus();
    })
    $(".loginBody .layui-form-item .layui-input").focus(function(){
        $(this).parent().addClass("layui-input-focus");
    })
    $(".loginBody .layui-form-item .layui-input").blur(function(){
        $(this).parent().removeClass("layui-input-focus");
        if($(this).val() != ''){
            $(this).parent().addClass("layui-input-active");
        }else{
            $(this).parent().removeClass("layui-input-active");
        }
    })
})

function getVerifyCode() {
    var url = "/vCode?" + Math.random();
    $.ajax({
        //请求方式
        type : "GET",
        //请求的媒体类型
        contentType: "application/json;charset=UTF-8",
        //请求地址
        url : url,
        //请求成功
        success : function(result) {
            console.log(result);
            $("#vCode").attr("src","data:image/png;base64," + result.img);
        },
        //请求失败，包含具体的错误信息
        error : function(e){
            console.log(e.status);
            console.log(e.responseText);
        }
    });
}
function mouseover(obj) {
    obj.style.cursor = "pointer";
}