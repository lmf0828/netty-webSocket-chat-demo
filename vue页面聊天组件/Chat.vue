<template>
  <div>

    <div style="width: 500px;line-height:80px;margin: auto;">
      <span style="font-size: 30px ">请加入聊天室</span>
      <el-input v-model="msg" placeholder="请输入你的昵称" style="width: 200px"></el-input>
      <el-button type="primary" @click="join">加入</el-button>

    </div>
    <JwChat-index :taleList="list"
                  @enter="bindEnter"
                  @clickTalk="talkEvent"
                  v-model="inputMsg"
                  :config="config"
                  :showRightBox=false
                  :quickList="quickList"
                  scrollType="scroll"
                  :winBarConfig="winBarConfig"
    >

    </JwChat-index>

  </div>
</template>


<script>
export default {
  data() {

    return {
      user: "",
      socket: "",
      msg: "",
      winBarConfig: {
        active: 'win00',  //当前选中
        width: '120px',
        listHeight: '60px',
        list: [ //{
          // id: 'win00',
          // img: '..//image/cover.png',
          // name: 'JwChat',
          // dept: '最简单、斯克莱德爱看书的最便',
          // readNum: 99
          //},
        ],
        callback: this.bindWinBar
      },
      config: {
        img: '',
        name: '',
        dept: '',
        callback: this.bindCover,
        historyConfig: {
          show: true,
          tip: '加载更多',
          callback: this.bindLoadHistory,
        },
      },
      inputMsg: '', //输入信息
      list: [],    //消息会话列表

      quickList: [    //输入框输入后  给快捷提示
        // {text: '这里是jwchat，您想了解什么问题。'},
        // {text: 'jwchat是最好的聊天组件'},
      ]
    }
  },
  methods: {
    init: function () {
      if (typeof (WebSocket) === "undefined") {
        alert("您的浏览器不支持socket")
      } else {
        // 实例化socket
        this.socket = new WebSocket("ws://localhost:8082/msg") //你的后端netty服务连接地址
        // 监听socket连接
        this.socket.onopen = this.open
        // 监听socket错误信息
        this.socket.onerror = this.error
        // 监听socket消息
        this.socket.onmessage = this.getMessage
      }
    },
    open: function () {
      console.log("socket连接成功")
    },
    error: function () {
      console.log("socket连接错误")
    },

    getMessage: function (msg) {
      var data = JSON.parse(msg.data);

      if (data.name === "系统消息") {

          this.$notify({
            title: data.name,
            message: data.msg,
            type: 'success'
          });

      } else if (data.name === "private") {
          console.log(data.from)

          const target =
            {
              "text": {"text": data.msg},
              "mine": false,
              "name": data.from,
              "img": "https://codegi.gitee.io/jwchatdoc/demo/image/three.jpeg"
            }


        //  this.list.push(target);
          var list = this.winBarConfig.list;
          var tmpUser={}
          for (let i = 0; i < list.length; i++) {
            if (list[i].name === data.from) {
              if (this.winBarConfig.active!==list[i].id){
                list[i].readNum++
              }
              tmpUser=list[i]

            }
          }

          //console.log(tmpUser)

          var item = sessionStorage.getItem(data.from);

          if (item != null) {

            var parse = JSON.parse(item);
            if (tmpUser.id===this.winBarConfig.active){
              this.list.push(target)
              sessionStorage.setItem(data.from, JSON.stringify(this.list))
            }else{
              parse.push(target)
              sessionStorage.setItem(data.from, JSON.stringify(parse))
            }
          }
          else {
            var tmp = []
            if (tmpUser.id===this.winBarConfig.active){
              this.list.push(target)
              sessionStorage.setItem(data.from, JSON.stringify(this.list))
            }else{
              tmp.push(target)
              sessionStorage.setItem(data.from, JSON.stringify(tmp))
            }

          }

      } else {

          this.$notify({
            title: data.name,
            message: "加入成功",
            type: 'success'
          });
          this.winBarConfig.list = []
          var users = JSON.parse(data.msg);


          for (let i = 0; i < users.length; i++) {
            let obj = {
              readNum: 0,
              id: i,
              name: users[i],
              img: 'https://ts3.cn.mm.bing.net/th?id=OIP-C.maao8hq8Cf4UDcIK_HMMNgAAAA&w=250&h=250&c=8&rs=1&qlt=90&o=6&dpr=1.3&pid=3.1&rm=2',
            }

            this.winBarConfig.list.push(obj);
          }
      }
    },

    join() {
      if (this.msg === '') {
        this.$message.error("请输入昵称")
      } else {
        this.user = this.msg
        this.socket.send(JSON.stringify({
          nickName: this.msg,
          code: 10003
        }))

      }
    },

    send: function () {

      this.socket.send(JSON.stringify({
        code: 10002,
        type: 1,
        from: this.user,
        target: this.config.name,
        content: this.inputMsg
      }))

    },
    close: function () {
      console.log("socket已经关闭")
    },

    destroyed() {
      // 销毁监听
      this.socket.onclose = this.close
    },

    bindWinBar(play = {}) {
      //console.log(play)
      this.list = []      //切换聊天会话时  置空

      const {type, data = {}} = play

      data.readNum = 0;

       var item = sessionStorage.getItem(data.name);    //查询聊天记录
       if (item!=null){
         this.list = JSON.parse(item)
       }


      // console.log(data);
      // console.log(type);
      if (type === 'winBar') {
        const {id, dept, name, img} = data
        this.config.img = img
        this.config.name = name
        this.winBarConfig.active = id
      }
    },
    bindLoadHistory() {

      const history = new Array(3).fill().map((i, j) => {
        return {
          "date": "2020/05/20 23:19:07",
          "text": {"text": j + new Date()},
          "mine": false,
          "name": "JwChat",
          "img": "https://www.baidu.com/img/flexible/logo/pc/result.png"
        }
      })
      let list = history.concat(this.list)
      this.list = list
      console.log('加载历史', list, history)
    },

    bindCover(type) {
      console.log('header')
    },

    bindEnter(e) {
      //console.log(e)      //发送/回车 事件    e: 数据
      const msg = this.inputMsg
      if (!msg) return;
      const msgObj = {
        // "date": new Date(),
        "text": {"text": msg},
        "mine": true,
        "name": this.user,
        "img": "https://codegi.gitee.io/jwchatdoc/demo/image/three.jpeg"
      }
      //this.list=this.list.concat(msgObj)
      this.send()   //发送消息

      this.list.push(msgObj) //将消息添加至聊天框中

      sessionStorage.setItem(this.config.name, JSON.stringify(this.list))  //保存与对方的聊天记录



    },
    toolEvent(type, obj) {
      console.log('tools', type, obj)
    },
    talkEvent(play) {    //点击对话框中头像触发
      console.log(play)
    },
  },
  mounted() {

    this.init()

    //const img = 'https://www.baidu.com/img/flexible/logo/pc/result.png'
    //   const list = [
    //     {
    //       "date": "2020/04/25 21:19:07",
    //       "text": { "text": sessionStorage.getItem("list") },
    //       "mine": true,
    //       "name": "留恋人间不羡仙",
    //       "img": "https://codegi.gitee.io/jwchatdoc/demo/image/three.jpeg"
    //     },
    //     {
    //       "date": "2021/03/02 13:14:21",
    //       "mine": false,
    //       "name": "留恋人间不羡仙",
    //       "img": img,
    //       "text": {
    //         system: {
    //           title: '在接入人工前，智能助手将为您首次应答。',
    //           subtitle: '猜您想问:',
    //           content: [
    //             {
    //               id: `system1`,
    //               text: '组件如何使用'
    //             },
    //             {
    //               id: `system2`,
    //               text: '组件参数在哪里查看'
    //             },
    //             {
    //               id: 'system',
    //               text: '我可不可把组件用在商业'
    //             }
    //           ]
    //         }
    //       }
    //     },
    // ]

    // this.list = list
  }
}
</script>
