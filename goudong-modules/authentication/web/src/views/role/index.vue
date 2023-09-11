<template>
  <div class="app-container">
    <!--  查询条件  -->
    <div class="filter-container">
      <div class="filter-item">
        <span class="filter-item-label">角色名称: </span>
        <RoleSelect ref="roleSelectRef" :role-multiple="true" @getSelectRoles="getSelectRoles" />
      </div>
      <div class="filter-item">
        <span class="filter-item-label">备注: </span>
        <el-input v-model="filter.remark" clearable placeholder="请输入" />
      </div>
      <div class="filter-item">
        <el-button
          v-permission="'sys:role:query'"
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
        <el-button v-permission="'sys:role:add'" class="el-button--small" icon="el-icon-plus" type="primary" @click="addRole">新增</el-button>
        <el-button v-permission="'sys:role:delete'" class="el-button--small" icon="el-icon-delete" type="danger" @click="deleteRoles">删除</el-button>
        <!--        <el-button class="el-button&#45;&#45;small" icon="el-icon-upload2">导入</el-button>
        <el-button class="el-button&#45;&#45;small" icon="el-icon-download">导出</el-button>-->
        <!--        <el-button class="el-button&#45;&#45;small" icon="el-icon-upload2" @click="importUserDialog=true">
          导入
        </el-button>
        <el-button class="el-button&#45;&#45;small" icon="el-icon-download" @click="exportExcel">
          导出
        </el-button>-->
      </div>
      <div class="right-tool">
        <el-tooltip class="right-tool-btn-tooltip" effect="dark" content="刷新" placement="top">
          <div class="right-tool-btn" @click="searchFunc">
            <i class="el-icon-refresh-right" />
          </div>
        </el-tooltip>
        <el-tooltip class="right-tool-btn-tooltip" effect="dark" content="密度" placement="top">
          <el-dropdown trigger="click" @command="changeElTableSizeCommand">
            <div class="right-tool-btn">
              <i class="el-icon-s-operation" />
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
      style="width: 100%"
      :data="role.roles"
      row-key="id"
      border
      :header-cell-style="{background:'#FAFAFA', color:'#000', height: '30px',}"
      :header-row-class-name="EL_TABLE.size"
      :size="EL_TABLE.size"
      @selection-change="selectionChangeFunc"
    >
      <el-table-column
        min-width="55"
        type="selection"
        header-align="center"
        align="center"
        class-name="selection"
      />
      <el-table-column
        label="序号"
        prop="serialNumber"
      >
      </el-table-column>
      <el-table-column
        label="角色名称"
        prop="name"
        sortable
      />
      <el-table-column
        label="用户数量"
        prop="userNumber"
        sortable
      >
        <template v-slot="scope">
          <el-link :underline="false"
                   type="primary"
                   @click="showUsers(scope.row)">{{scope.row.userNumber}}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        label="备注"
        prop="remark"
        show-overflow-tooltip
      />
      <el-table-column
        label="创建时间"
        prop="createdDate"
        sortable
      />
      <el-table-column
        label="操作"
        align="center"
      >
        <template v-slot="scope">
          <div class="el-link-parent">
            <el-link
              v-permission="'sys:role:edit'"
              icon="el-icon-edit"
              :underline="false"
              type="primary"
              :disabled="Number(scope.row.id) < 2147483647"
              @click="editRole(scope.row)"
            >编辑</el-link>
            <el-link
              v-permission="'sys:role:permission'"
              icon="el-icon-finished"
              :underline="false"
              type="primary"
              :disabled="Number(scope.row.id) < 2147483647"
              @click="editRoleMenu(scope.row)"
            >权限</el-link>
            <el-link
              v-permission="'sys:role:delete'"
              icon="el-icon-delete"
              :underline="false"
              type="danger"
              :disabled="Number(scope.row.id) < 2147483647"
              @click="deleteRole(scope.row.id)"
            >删除</el-link>
          </div>
        </template>
      </el-table-column>
      <!--隐藏-->
      <el-table-column
        v-if="false"
        label="用户集合"
        width="300"
        prop="users"
      />
    </el-table>
    <!-- 分页控件 -->
    <el-pagination
      :current-page="role.page"
      :page-size="role.size"
      :page-sizes="role.pageSizes"
      :total="role.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
    <!--  新增角色弹窗  -->
    <CreateRoleDialog :create-role-dialog.sync="createRoleDialog" />
    <!--编辑角色弹窗-->
    <EditRoleDialog :edit-role-dialog.sync="editRoleDialog" :edit-role-info="editRoleInfo" />
    <!--编辑角色权限弹窗-->
    <EditRoleMenuDialog :edit-role-menu-dialog.sync="editRoleMenuDialog" :edit-role-menu-info="editRoleMenuInfo" />
    <!--  拥有角色的详细用户弹窗  -->
    <el-dialog :title="roleUserDialog.title" :visible.sync="roleUserDialog.visible" width="460px">
      <el-table
        :data="roleUserDialog.data"
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
          class-name="a"
          label="用户ID"
          prop="id"
        />
        <el-table-column
          label="用户名"
          prop="name"
        />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import waves from '@/directive/waves' // waves directive
import { deleteRoleByIdsApi, pageRolesApi } from '@/api/role'
import { initMenuApi } from '@/api/menu'

import { isNotEmpty } from "@/utils/assertUtil";

export default {
  name: 'RolePage',
  components: {
    RoleSelect: () => import('@/components/User/RoleSelect'),
    CreateRoleDialog: () => import('@/views/role/components/CreateRoleDialog'),
    EditRoleDialog: () => import('@/views/role/components/EditRoleDialog'),
    EditRoleMenuDialog: () => import('@/views/role/components/EditRoleMenuDialog'),
  },
  directives: { waves },
  data() {
    return {
      isLoading: false,
      // 表格中的角色
      role: {
        roles: [],
        page: 1,
        size: this.$globalVariable.PAGE_SIZES[0],
        total: 0,
        totalPage: 0,
        pageSizes: this.$globalVariable.PAGE_SIZES
      },
      filter: {
        ids: [],
        name: undefined,
        remark: undefined,
      },
      // 复选框选中的用户
      checkRoleIds: [],
      createRoleDialog: false, // 创建角色弹窗
      editRoleDialog: false, // 编辑角色弹窗
      editRoleInfo: undefined, // 编辑角色弹窗的数据
      editRoleMenuDialog: false, // 编辑角色权限弹窗
      editRoleMenuInfo: {}, // 编辑角色权限弹窗的数据
      roleUserDialog:{ // 角色下的用户信息弹窗
        title: '',
        visible: false,
        data: undefined
      },
      elDropdownItemClass: ['el-dropdown-item--click', undefined, undefined],
      EL_TABLE: {
        // 显示大小
        size: 'medium'
      },
    }
  },
  mounted() {
    // 优先加载表格数据
    this.loadPageRole()
  },
  methods: {
    getSelectRoles(selectedRoles) {
      console.log("selectedRoles", selectedRoles)
      this.filter.ids = selectedRoles.map(role => role.value)
    },
    searchFunc() {
      // 点击查询按钮
      this.role.page = 1
      this.loadPageRole()
    },
    // 点击重置按钮
    resetSearchFilter() {
      // 清空子组件（用户名下拉）值
      this.$refs.roleSelectRef.reset();
      // 赋默认值
      this.filter = {};
    },
    loadPageRole() {
      this.isLoading = true;
      const pageParam = {
        page: this.role.page,
        size: this.role.size,
        ids: this.filter.ids,
        name: this.filter.name,
        remark: this.filter.remark,
      }
      pageRolesApi(pageParam).then(data => {
        const content = data.content
        // 修改分页组件
        this.role.page = Number(data.page)
        this.role.size = Number(data.size)
        this.role.total = Number(data.total)
        this.role.totalPage = Number(data.totalPage)

        // 将原先的数据丢弃
        this.role.roles = []

        // 添加到数据集合
        content.forEach((item, index, arr) => {
          const column = {
            userNumber: item.users.length,
            ...item
          }
          this.role.roles.push(column)
        })
      }).finally(() => {
        this.isLoading = false;
      })
    },
    // 复选框勾选事件
    selectionChangeFunc(roles) {
      const ids = roles.map(m => m.id)
      this.checkRoleIds = ids
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
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
      this.role.size = val
      this.loadPageRole()
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`)
      this.role.page = val
      this.loadPageRole()
    },
    showUsers(row){
      console.log(row.users)
      this.roleUserDialog.title = `角色 ${row.name} 拥有 ${row.users.length} 人`
      this.roleUserDialog.data = row.users
      this.roleUserDialog.visible = true
    },
    generate(item) {
      const obj = {
        name: item.name,
        type: item.type,
        openModel: item.openModel,
        path: item.path,
        permissionId: item.permissionId,
        method: item.method,
        metadata: item.meta,
      }
      if (item.meta) {
        if (item.meta.icon) {
          obj.icon = item.meta.icon
        }
        if (!item.meta.title) {
          obj.metadata.title = obj.name
        }

        obj.metadata = JSON.stringify(obj.metadata)
      }
      // 菜单（组件）未单独配置组件路由
      if (item.type === 1 && !item.componentPath) {
        obj.componentPath = item.path
      }

      if (item.children) {
        // 子元素
        obj.children = [];
        item.children.forEach((i, index, arr) => {
          obj.children.push(this.generate(i));
        })
      }
      return obj;
    },
    // 新增角色
    addRole() {
      this.createRoleDialog = true
    },
    // 批量删除用户
    deleteRoles() {
      const ids = this.checkRoleIds;
      isNotEmpty(ids, () => this.$message.warning("请勾选需要删除的角色"))
        .then(() => {
          this.$confirm('此操作将永久删除所选角色, 是否继续?', '删除', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            deleteRoleByIdsApi(ids).then(data => {
              this.$message.success("删除成功")
              this.loadPageUser()
            })
          }).catch(() => {
            this.$message.info("已取消删除");
          })
        }).catch(() => {});
    },
    // 删除角色
    deleteRole(id) {
      this.$confirm('此操作将永久删除该角色, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRoleByIdsApi([id]).then(response => {
          this.$message.success("删除成功")
          this.loadPageRole()
        })
      }).catch(() => {
        this.$message.info("已取消删除");
      })
    },
    // 修改角色
    editRole(row) {
      this.editRoleInfo = row
      this.editRoleDialog = true
    },
    // 设置权限
    editRoleMenu(row) {
      this.editRoleMenuInfo = row
      this.editRoleMenuDialog = true
    }
  }

}
</script>
