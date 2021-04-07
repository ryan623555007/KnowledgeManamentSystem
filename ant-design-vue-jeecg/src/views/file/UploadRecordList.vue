<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="标题">
              <!-- <a-input placeholder="请输入标题" v-model="queryParam.title"></a-input> -->
              <j-input placeholder="请输入标题" v-model="queryParam.title"></j-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="文件类别">
              <j-dict-select-tag placeholder="请选择文件类别" v-model="queryParam.fileClass" dictCode="file_class"/>
            </a-form-item>
          </a-col>
          <!-- <template>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="审批状态">
                <j-dict-select-tag placeholder="请选择审批状态" v-model="queryParam.fileStatus" v-decorator="['fileStatus', '1']" :triggerChange="true"   />
              </a-form-item>
            </a-col>
          </template> -->
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <!-- <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a> -->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('上传文件记录表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload> -->
      <!-- 高级查询区域 -->
      <!-- <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query> -->
      <!-- <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>  -->
    </div>

    <!-- table区域-begin -->
    <div>
    

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <!-- <a @click="handleEdit(record)">编辑</a> -->
          <a @click="handleDetail(record)">详情</a>
          <a-divider type="vertical" />
          <!-- <a @click="handleCollectDoc(record)">收藏</a> -->
          <a @click="handleCollectDoc(record)" v-show="record.collectStatus==1">收藏</a>
          <a @click="handleDeleteCollect(record)" v-show="record.collectStatus==0">已收藏</a>
          <!-- <a-divider type="vertical" />
            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm> -->
          <a-divider type="vertical" />
            <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a v-has="'online:upload_record:delete'">删除</a>
            </a-popconfirm>
          <!-- <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
              
              </a-menu-item>
            </a-menu>
          </a-dropdown> -->
        </span>

      </a-table>
    </div>

    <upload-record-modal ref="modalForm" @ok="modalFormOk"></upload-record-modal>
  </a-card>
</template>

<script>
  import { getAction, postAction,deleteAction } from '@/api/manage'
  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import UploadRecordModal from './modules/UploadRecordModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import JInput from '@/components/jeecg/JInput'
  export default {
    name: 'UploadRecordList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      UploadRecordModal,
      JInput
    },
    data () {
      return {
        queryParam:{
            title:'',
            fileClass:'',
            fileStatus:'1',
            // createBy: this.$store.getters.userInfo.username
        },
        description: '上传文件记录表管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'标题',
            align:"center",
            dataIndex: 'title'
          },
          {
            title:'文件类别',
            align:"center",
            dataIndex: 'fileClass_dictText'
          },
          {
            title:'描述',
            align:"center",
            dataIndex: 'description'
          },
          {
            title:'审批状态',
            align:"center",
            dataIndex: 'fileStatus_dictText'
          },
          // {
          //   title:'文件',
          //   align:"center",
          //   dataIndex: 'files',
          //   scopedSlots: {customRender: 'fileSlot'}
          // },
          {
            title:'上传者',
            align:"center",
            dataIndex: 'createBy'
          },
          {
            title:'创建日期',
            align:"center",
            dataIndex: 'createTime'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/UploadRecord/uploadRecord/list",
          delete: "/UploadRecord/uploadRecord/delete",
          deleteBatch: "/UploadRecord/uploadRecord/deleteBatch",
          exportXlsUrl: "/UploadRecord/uploadRecord/exportXls",
          importExcelUrl: "UploadRecord/uploadRecord/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
   
    this.getSuperFieldList();
    debugger
     this.$store.getters.userInfo.username
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'title',text:'标题',dictCode:''})
        fieldList.push({type:'string',value:'fileClass',text:'文件类别',dictCode:'file_class'})
        fieldList.push({type:'string',value:'description',text:'描述',dictCode:''})
        fieldList.push({type:'string',value:'fileStatus',text:'审批状态',dictCode:'file_status'})
        fieldList.push({type:'Text',value:'files',text:'文件',dictCode:''})
        fieldList.push({type:'string',value:'createBy',text:'上传者',dictCode:''})
        fieldList.push({type:'datetime',value:'createTime',text:'创建日期'})
        this.superFieldList = fieldList
      },handleCollectDoc(record){
        record.collectStatus ='0'; // 已收藏
        record.createBy=this.$store.getters.userInfo.username;
        postAction(`/collectFileRecord/add`, record)
        .then((res) => {
          if (res.success) {
            this.$message.success('已收藏')
            this.searchQuery()
          } else {
            this.$message.error(res.message)
            this.requestCodeSuccess = false
          }
        })
        .catch(() => {
          this.requestCodeSuccess = false
        })
    },handleDeleteCollect(record){
      record.createBy=this.$store.getters.userInfo.username;
      postAction(`/collectFileRecord/deleteByField`,record )
        .then((res) => {
          if (res.success) {
            this.$message.success('已取消收藏')
            this.searchQuery()
          } else {
            this.$message.error(res.message)
            this.requestCodeSuccess = false
          }
        })
        .catch(() => {
          this.requestCodeSuccess = false
        })
    }

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>