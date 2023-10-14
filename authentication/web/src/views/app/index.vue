<!--应用管理-->
<template>
  <div class="app-container">
    <!--  查询条件  -->
    <div class="filter-container">
      <div class="filter-item">
        <span class="filter-item-label">应用ID: </span>
        <el-input v-model="filter.id" clearable placeholder="请输入"/>
      </div>
      <div class="filter-item">
        <span class="filter-item-label">应用名称: </span>
        <el-input v-model="filter.name" clearable placeholder="请输入"/>
      </div>
      <div class="filter-item">
        <span class="filter-item-label">首页地址: </span>
        <el-input v-model="filter.homePage" clearable placeholder="请输入"/>
      </div>
      <div class="filter-item">
        <span class="filter-item-label">状态: </span>
        <el-select
          v-model="filter.enabled"
          clearable
        >
          <el-option
            v-for="item in appStatus"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
      <div class="filter-item">
        <span class="filter-item-label">备注: </span>
        <el-input v-model="filter.remark" clearable placeholder="请输入"/>
      </div>
      <div class="filter-item">
        <el-button
          v-permission="'sys:app:query'"
          icon="el-icon-search"
          type="primary"
          @click="searchFunc"
        >
          查询
        </el-button>
      </div>
      <div class="filter-item">
        <!--不加icon会小一个像素的高度-->
        <el-button icon="el-icon-setting" @click="resetSearchFilter">重置</el-button>
      </div>
    </div>
    <!--顶部操作栏-->
    <div class="el-table-tool">
      <div class="left-tool">
        <el-button v-permission="'sys:app:apply'" class="el-button--small" icon="el-icon-plus" type="primary"
                   @click="dialog.create.open=true">
          新增
        </el-button>
        <el-button class="el-button--small" icon="el-icon-document" type="primary"
                   @click="dialog.createCert.open=true">
          创建证书
        </el-button>
      </div>
      <div class="right-tool">
        <el-tooltip class="right-tool-btn-tooltip" effect="dark" content="刷新" placement="top">
          <div class="right-tool-btn" @click="load">
            <i class="el-icon-refresh-right"/>
          </div>
        </el-tooltip>
        <el-tooltip class="right-tool-btn-tooltip" effect="dark" content="密度" placement="top">
          <el-dropdown trigger="click" @command="changeElTableSizeCommand">
            <div class="right-tool-btn">
              <i class="el-icon-s-operation"/>
            </div>
            <el-dropdown-menu slot="dropdown" size="small">
              <el-dropdown-item :class="elDropdownItemClass[0]" command="0,medium">默认</el-dropdown-item>
              <el-dropdown-item :class="elDropdownItemClass[1]" command="1,small">中等</el-dropdown-item>
              <el-dropdown-item :class="elDropdownItemClass[2]" command="2,mini">紧凑</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-tooltip>
      </div>
    </div>
    <!-- 表格  -->
    <el-table
      ref="table"
      v-loading="isLoading"
      border
      :data="app.apps"
      row-key="id"
      style="width: 100%"
      :header-cell-style="{background:'#FAFAFA', color:'#000', height: '30px',}"
      :header-row-class-name="EL_TABLE.size"
      :size="EL_TABLE.size"
    >
      <el-table-column
        width="55"
        type="selection"
        header-align="center"
        align="center"
        class-name="selection"
      />
      <el-table-column
        fixed
        label="序号"
        width="50"
        align="center"
      >
        <template v-slot="scope">
          {{ (app.page - 1) * app.size + (scope.$index + 1) }}
        </template>
      </el-table-column>
      <el-table-column
        label="应用id"
        prop="id"
        sortable
      />
      <el-table-column
        label="应用密钥"
        prop="secret"
        sortable
      />
      <el-table-column
        label="应用名称"
        prop="name"
        sortable
      />
      <el-table-column
        label="首页地址"
        prop="homePage"
        show-overflow-tooltip
      />
      <el-table-column
        label="状态"
        prop="status"
        align="center"
        sortable
      >
        <template v-slot="scope">
          <span v-if="scope.row.enabled == true">已激活</span>
          <span v-else>未激活</span>
        </template>
      </el-table-column>
      <el-table-column
        label="备注"
        prop="remark"
        sortable
        show-overflow-tooltip
      />
      <el-table-column
        label="创建时间"
        prop="createdDate"
        sortable
      />
      <!--操作-->
      <el-table-column
        label="操作"
        align="center"
      >
        <template v-slot="scope">
          <div class="el-link-parent">
            <el-link
              icon="el-icon-edit"
              :underline="false"
              type="primary"
              @click="dialogCertOpen(scope.row)"
            >证书
            </el-link>
            <el-link
              v-permission="'sys:app:audit'"
              icon="el-icon-edit"
              :underline="false"
              type="primary"
              :disabled="Number(scope.row.id) === 1"
              @click="update(scope.row)"
            >修改
            </el-link>
            <el-link
              v-permission="'sys:app:delete'"
              icon="el-icon-delete"
              :underline="false"
              type="danger"
              :disabled="Number(scope.row.id) === 1"
              @click="deleteRow(scope.row)"
            >删除
            </el-link>
          </div>
        </template>
      </el-table-column>
      <!--隐藏-->
      <el-table-column
        v-if="false"
        label="id"
        prop="id"
      />
    </el-table>
    <!-- 分页控件 -->
    <el-pagination
      :current-page="app.page"
      :pager-count="app.pagerCount"
      :page-size="app.size"
      :page-sizes="app.pageSizes"
      :total="app.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!--  申请应用弹窗  -->
    <el-dialog title="申请应用" width="600px" :visible.sync="dialog.create.open" @close="dialog.create.open = false">
      <el-form ref="createForm" :model="dialog.create.form.data" :rules="dialog.create.form.rules" label-width="80px">
        <el-form-item label="应用名" prop="name">
          <el-input v-model="dialog.create.form.data.name" placeholder="请输入应用名称" clearable/>
        </el-form-item>
        <el-form-item label="首页" prop="homePage">
          <el-input v-model="dialog.create.form.data.homePage" placeholder="请输入访问应用首页web地址"  clearable/>
        </el-form-item>
        <el-form-item label="状态" prop="enabled">
          <el-select
            v-model="dialog.create.form.data.enabled"
            placeholder="请选择应用状态"
            clearable
          >
            <el-option
              v-for="item in appStatus"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dialog.create.form.data.remark"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogCreateCancel">取 消</el-button>
        <el-button type="primary" @click="dialogCreateSubmit()">确 定</el-button>
      </div>
    </el-dialog>

    <!--  修改应用弹窗  -->
    <el-dialog title="修改应用" width="600px" :visible.sync="dialog.update.open" @close="dialogUpdateCancel()">
      <el-form ref="updateForm" :model="dialog.update.form.data" :rules="dialog.update.form.rules" label-width="80px">
        <el-form-item label="应用名" prop="name">
          <el-input v-model="dialog.update.form.data.name" placeholder="请输入应用名称" disabled clearable/>
        </el-form-item>
        <el-form-item label="首页" prop="homePage">
          <el-input v-model="dialog.update.form.data.homePage" placeholder="请输入访问应用首页web地址" clearable/>
        </el-form-item>
        <el-form-item label="状态" prop="enabled">
          <el-select
            v-model="dialog.update.form.data.enabled"
            placeholder="请选择应用状态"
            clearable
          >
            <el-option
              v-for="item in appStatus"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dialog.update.form.data.remark"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogUpdateCancel">取 消</el-button>
        <el-button type="primary" @click="dialogUpdateSubmit()">确 定</el-button>
      </div>
    </el-dialog>

    <!--  新增证书  -->
    <el-dialog title="新增证书" width="600px" :visible.sync="dialog.createCert.open" @close="dialog.createCert.open = false">
      <el-form ref="createCert" :model="dialog.createCert.form.data" :rules="dialog.createCert.form.rules" label-width="80px">
        <el-form-item label="选择应用" prop="appId">
          <el-select v-model="dialog.createCert.form.data.appId" placeholder="请选择应用" clearable>
            <el-option
              v-for="item in apps"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="截止时间" prop="validTime">
          <el-date-picker v-model="dialog.createCert.form.data.validTime" type="datetime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd HH:mm:ss" placeholder="请选择证书的截止时间" :picker-options="dialog.createCert.pickerOptions" clearable />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="dialog.createCert.form.data.remark" placeholder="请输入备注" clearable />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createCertCancel">取 消</el-button>
        <el-button type="primary" @click="createCertSubmit()">确 定</el-button>
      </div>
    </el-dialog>

    <!--  应用证书列表  -->
    <el-dialog title="证书" width="600px" :visible.sync="dialog.cert.open" @close="dialog.cert.open = false">
      <el-table
        :data="dialog.cert.data"
        row-key="id"
        style="width: 100%"
        max-height="480"
        border
        :header-cell-style="{background:'#FAFAFA', color:'#000', height: '30px',}"
        :header-row-class-name="EL_TABLE.size"
        :size="EL_TABLE.size"
      >
        <el-table-column
          class-name="a"
          label="序号"
          width="50"
        >
          <template v-slot="scope">
            {{ scope.$index + 1}}
          </template>
        </el-table-column>
        <el-table-column
          label="序列号"
          prop="serialNumber"
        />
        <el-table-column
          label="截止时间"
          prop="validTime"
        />
        <el-table-column
          align="center"
          label="操作"
        >
          <template v-slot="scope">
            <el-link
              icon="el-icon-download"
              :underline="false"
              type="primary"
              @click="downloadCert(scope.row)"
            >下载
            </el-link>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>

import {ENABLED_ARRAY} from "@/constant/commons";
import {createAppApi, createCertApi, deleteAppApi, listCertsApi, pageAppsApi, updateAppApi} from "@/api/app";
import { dropDownAllAppApi } from '@/api/dropDown';
import {Message} from "element-ui";
import {FutureTime, WebUrl} from "@/utils/ElementValidatorUtil"

export default {
  name: 'App',
  components: {},
  data() {
    return {
      appStatus: ENABLED_ARRAY,
      filter: {
        id: undefined,
        name: undefined,
        homePage: undefined,
        enabled: undefined,
        remark: undefined
      },
      app: {
        apps: [],
        page: 1,
        size: this.$globalVariable.PAGE_SIZES[0],
        pagerCount: this.$globalVariable.PAGER_COUNT,
        total: 0,
        totalPage: 0,
        pageSizes: this.$globalVariable.PAGE_SIZES
      },
      isLoading: false,
      elDropdownItemClass: ['el-dropdown-item--click', undefined, undefined],
      EL_TABLE: {
        // 显示大小
        size: 'medium'
      },
      apps: [],
      // 弹窗相关
      dialog: {
        create: { // 新增/创建
          open: false, // 是否打开窗口
          form: {
            data: {
              name: undefined,
              homePage: undefined,
              enabled: undefined,
              remark: undefined
            }, // 弹窗的数据
            rules: { // 弹窗的规则
              name: [
                {required: true, message: '请填写应用名称', trigger: 'blur'},
              ],
              homePage: [
                {required: true, message: '请填写应用首页web地址', trigger: 'blur'},
                {max: 255, message: '长度在 0 到 255 个字符', trigger: 'blur'},
                {validator: WebUrl, trigger: 'blur'}
              ],
              enabled: [
                {required: true, message: '请选择应用状态', trigger: 'blur'},
              ]
            }
          }
        },
        update: { // 修改
          open: false, // 是否打开窗口
          form: {
            data: {
              id: undefined,
              name: undefined,
              homePage: undefined,
              enabled: undefined,
              remark: undefined
            }, // 弹窗的数据
            rules: { // 弹窗的规则
              name: [
                {required: true, message: '请填写应用名称', trigger: 'blur'},
              ],
              homePage: [
                {required: true, message: '请填写应用首页web地址', trigger: 'blur'},
                {max: 255, message: '长度在 0 到 255 个字符', trigger: 'blur'},
                {validator: WebUrl, trigger: 'blur'}
              ],
              enabled: [
                {required: true, message: '请选择应用状态', trigger: 'blur'},
              ]
            }
          }
        },
        createCert:{ // 创建证书
          open: false, // 是否打开窗口
          pickerOptions: {
            disabledDate(v) {
              return v.getTime() < new Date().getTime() - 86400000;//  - 86400000是否包括当天
            }
          },
          form:{
            data: {},
            rules: {
              appId: [
                {required: true, message: '请选择应用', trigger: 'blur'},
              ],
              validTime: [
                {required: true, message: '请填写过期时间', trigger: 'blur'},
                {validator: FutureTime, trigger: 'blur'},
              ],
            }
          },
        },
        cert:{ // 证书列表
          open: false, // 是否打开窗口
          data:[]
        }
      }
    }
  },
  mounted() {
    // 优先加载表格数据
    this.load()
    // 强制渲染，解决表格 固定列后，列错位问题
    this.$nextTick(() => {
      this.$refs.table.doLayout()
    })
    // 获取应用下拉
    dropDownAllAppApi().then(data => {
      console.log(data);
      this.apps = data
    })
  },
  methods: {
    load() {
      this.isLoading = true;
      const pageParam = {
        page: this.app.page,
        size: this.app.size,
        ...this.filter
      }
      pageAppsApi(pageParam).then(data => {
        const content = data.content

        // 修改分页组件
        this.app.page = Number(data.page)
        this.app.size = Number(data.size)
        this.app.total = Number(data.total)
        this.app.totalPage = Number(data.totalPage)

        // 将原先的数据丢弃
        this.app.apps = []

        // 添加到数据集合
        content.forEach((item, index, arr) => {
          const column = {
            ...item,
          }

          this.app.apps.push(column)
        })
      }).finally(() => {
        this.isLoading = false;
      })
    },
    // 点击查询按钮
    searchFunc() {
      this.app.page = 1
      this.load()
    },
    // 点击重置按钮
    resetSearchFilter() {
      // 赋默认值
      this.filter = {
        id: undefined,
        name: undefined,
        enabled: undefined,
        remark: undefined
      };
    },
    // 修改表格大小
    changeElTableSizeCommand(val) {
      const args = val.split(",");
      const idx = Number(args[0]);
      console.log(args)
      this.elDropdownItemClass.map((value, index, array) => {
        if (index === idx) {
          array[index] = "el-dropdown-item--click";
        } else {
          array[index] = undefined;
        }
      })
      console.log(this.elDropdownItemClass)
      this.elDropdownItemClass[args[0]]
      this.EL_TABLE.size = args[1];
    },
    // 更改每页显示多少条
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
      this.app.size = val
      this.load()
    },
    // 修改当前页码
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`)
      this.app.page = val
      this.load()
    },

    // 弹窗
    // 新增
    dialogCreateCancel() {
      this.dialog.create.open = false
      this.dialog.create.form.data = {}
    },
    dialogCreateSubmit() {
      this.$refs.createForm.validate((valid) => {
        if (valid) {
          createAppApi(this.dialog.create.form.data).then(response => {
            // 保存成功
            Message({
              message: '应用创建成功',
              type: 'success',
            })
            this.dialogCreateCancel()
            this.load();
          })
        } else {
          return false;
        }
      });
    },

    // 修改
    update(row) {
      this.dialog.update.open = true
      this.dialog.update.form.data = { ...row }
      this.dialog.update.form.data.enabled = row.enabled ? 1 : 0;
    },
    dialogUpdateCancel() {
      this.dialog.update.open = false
      this.dialog.update.form.data = {}
    },
    dialogUpdateSubmit() {
      this.$refs.updateForm.validate((valid) => {
        if (valid) {
          updateAppApi(this.dialog.update.form.data).then(response => {
            // 保存成功
            Message({
              message: '应用修改成功',
              type: 'success',
            })
            this.dialogUpdateCancel()
            this.load();
          })
        } else {
          return false;
        }
      });
    },
    // 删除行
    deleteRow(row) {
      this.$confirm('此操作将永久删除该应用, 是否继续?', '删除', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteAppApi(row.id).then(data => {
          this.$message.success("删除成功")
          this.load()
        })
      }).catch(() => {
        this.$message.info("已取消删除");
      })
    },
    // 创建证书 -- 取消
    createCertCancel(){
      this.dialog.createCert.open = false;
      this.dialog.createCert.form.data = {};
    },
    // 创建证书 -- 提交
    createCertSubmit(){
      console.log("创建证书：", this.dialog.createCert.form.data)
      this.$refs.createCert.validate((valid) => {
        console.log("valid", valid)
        if (valid) {
          createCertApi(this.dialog.createCert.form.data).then(response => {
            // 保存成功
            Message({
              message: '创建成功',
              type: 'success',
            })
            this.createCertCancel()
          })
        } else {
          return false;
        }
      });
    },

    dialogCertOpen(row) {
      console.log("查询证书" + row)
      listCertsApi(row.id).then(certs => {
        console.log(certs)
        this.dialog.cert.data = certs.map(m => {
          return {serialNumber: m.serialNumber, validTime: m.validTime, cert: m.cert, appName: row.name}
        })
        this.dialog.cert.open = true;
      })
    },
    // 下载证书
    downloadCert(row) {
      console.log(row)
      // 获取响应数据
      const blob = new Blob(
        [row.cert], // 将获取到的二进制文件转成blob
        { type: 'charset=utf-8' } // 有时候打开文档会出现乱码，设置一下字符编码
      );
      // 获取文件名
      let filename = row.appName + ".cer";

      // 转成文件流之后，可以通过模拟点击实现下载效果
      const element = document.createElement('a'); // js创建一个a标签
      const href = window.URL.createObjectURL(blob); 					// 文档流转化成Base64
      element.href = href;
      element.download = filename; 								// 下载后文件名
      document.body.appendChild(element);
      element.click(); 												// 点击下载
      document.body.removeChild(element); 							// 下载完成移除元素
      window.URL.revokeObjectURL(href);
    }
  }
}
</script>

<style lang="scss">

</style>
