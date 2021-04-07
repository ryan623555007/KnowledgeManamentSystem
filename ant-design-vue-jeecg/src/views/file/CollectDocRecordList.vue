<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="文档类别">
              <j-category-select placeholder="请选择文档类别" v-model="queryParam.docType" pcode="B03"/>
            </a-form-item>
          </a-col>
           <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="文件名称">
              <!-- <a-input placeholder="请输入文件名称" v-model="queryParam.fileName"></a-input> -->
              <j-input placeholder="请输入文件名称" v-model="queryParam.fileName"></j-input>
            </a-form-item>
          </a-col>
           <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="SQL">
              <!-- <a-input placeholder="请输入文件名称" v-model="queryParam.fileName"></a-input> -->
              <j-input placeholder="请输入sql" v-model="queryParam.sqlText"></j-input>
            </a-form-item>
          </a-col>
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
      <!-- <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('文档记录收藏表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload> -->
      <!-- 高级查询区域 -->
      <!-- <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown> -->
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
            <a-popconfirm title="确定取消收藏吗?" @confirm="() => handleDeleteCollect(record)">
              <a>取消收藏</a>
            </a-popconfirm>
            
          <!-- <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown> -->
        </span>

      </a-table>
    </div>

    <collect-doc-record-modal ref="modalForm" @ok="modalFormOk"></collect-doc-record-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { getAction, postAction,deleteAction } from '@/api/manage'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  // import CollectDocRecordModal from './modules/CollectDocRecordModal'
  import { loadCategoryData } from '@/api/api'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
import JInput from '@/components/jeecg/JInput'
  export default {
    name: 'CollectDocRecordList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      // CollectDocRecordModal,
      JInput
    },
    data () {
      return {
          queryParam:{
             createBy: this.$store.getters.userInfo.username

        },
        description: '文档记录收藏表管理页面',
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
            title:'文件名称',
            align:"center",
            dataIndex: 'fileName'
          },
          {
            title:'文档类别',
            align:"center",
            dataIndex: 'docType',
            customRender: (text) => (text ? filterMultiDictText(this.dictOptions['docType'], text) : '')
          },
          
          {
            title:'描述',
            align:"center",
            dataIndex: 'description'
          },
          {
            title:'sql语句',
            align:"center",
            dataIndex: 'sqlText',
            scopedSlots: {customRender: 'htmlSlot'}
            
          },
          // {
          //   title:'收藏状态',
          //   align:"center",
          //   dataIndex: 'collectStatus'
          // },
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
          list: "/collectDocRecord/list",
          delete: "/collectDocRecord/delete",
          deleteBatch: "/collectDocRecord/deleteBatch",
          exportXlsUrl: "/collectDocRecord/exportXls",
          importExcelUrl: "org.jeecg.modules/collectDocRecord/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
        loadCategoryData({code:'B03'}).then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'docType', res.result)
          }
        })
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'docType',text:'文档类别'})
        fieldList.push({type:'string',value:'fileName',text:'文件名称',dictCode:''})
        fieldList.push({type:'string',value:'description',text:'描述',dictCode:''})
        fieldList.push({type:'Text',value:'sqlText',text:'sql语句',dictCode:''})
        fieldList.push({type:'string',value:'collectStatus',text:'收藏状态',dictCode:''})
        this.superFieldList = fieldList
      },handleDeleteCollect(record){
      postAction(`/collectDocRecord/deleteByField`,record )
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