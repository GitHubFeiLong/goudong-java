<!--菜单页面-->
<template>
  <div class="app-container">
    <!--  查询条件  -->
    <div class="filter-container">
      <div class="filter-item">
        <span class="filter-item-label">菜单名称: </span>
        <el-input v-model="filter.name" clearable placeholder="请输入" />
      </div>
      <div class="filter-item">
        <span class="filter-item-label">菜单类型: </span>
        <el-select v-model="filter.type" clearable>
          <el-option
            v-for="item in menuTypeArray"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </div>
      <div class="filter-item">
        <span class="filter-item-label">权限标识: </span>
        <el-input v-model="filter.permissionId" clearable placeholder="请输入" />
      </div>
      <div class="filter-item">
        <span class="filter-item-label">资源路径: </span>
        <el-input v-model="filter.path" clearable placeholder="请输入" />
      </div>
      <div class="filter-item">
        <el-button
          v-permission="'sys:user:query'"
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
        <el-button v-permission="'sys:menu:init'" class="el-button--small" icon="el-icon-edit" type="primary"
                   @click="initMenu"
        >初始菜单
        </el-button>
        <el-button v-permission="'sys:menu:add'" class="el-button--small" icon="el-icon-plus" type="primary"
                   @click="addMenu"
        >新增
        </el-button>
      </div>
      <div class="right-tool">
        <el-tooltip class="right-tool-btn-tooltip" effect="dark" content="刷新" placement="top">
          <div class="right-tool-btn">
            <i class="el-icon-refresh-right"/>
          </div>
        </el-tooltip>
        <el-tooltip class="right-tool-btn-tooltip" effect="dark" content="密度" placement="top">
          <el-dropdown trigger="click" @command="changeElTableSizeCommand">
            <div class="right-tool-btn">
              <i class="el-icon-s-operation"/>
            </div>
            <el-dropdown-menu slot="dropdown" size="small">
              <el-dropdown-item :class="table.elDropdownItemClass[0]" command="0,medium">默认</el-dropdown-item>
              <el-dropdown-item :class="table.elDropdownItemClass[1]" command="1,small">中等</el-dropdown-item>
              <el-dropdown-item :class="table.elDropdownItemClass[2]" command="2,mini">紧凑</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </el-tooltip>
      </div>
    </div>
    <!-- 表格  -->
    <el-table
      ref="table"
      v-loading="table.isLoading"
      border
      :data="table.data"
      row-key="id"
      style="width: 100%"
      :header-cell-style="{background:'#FAFAFA', color:'#000', height: '30px',}"
      :header-row-class-name="table.EL_TABLE.size"
      :size="table.EL_TABLE.size"
    >
      <el-table-column
        label="序号"
        prop="serialNumber"
      />
      <el-table-column
        label="权限标识"
        width="75"
        prop="permissionId"
        sortable
      />
      <el-table-column
        label="名称"
        min-width="50"
        prop="name"
        sortable
        show-overflow-tooltip
      />
      <el-table-column
        label="类型"
        width="170"
        prop="type"
        sortable
      />
      <el-table-column
        label="资源路径"
        width="170"
        prop="path"
        sortable
      />
      <el-table-column
        label="请求方式"
        width="170"
        prop="method"
        sortable
      />
      <el-table-column
        label="排序"
        width="170"
        prop="sortNum"
        sortable
      />
      <el-table-column
        label="是否是隐藏菜单"
        width="170"
        prop="hide"
        sortable
      />
      <el-table-column
        label="创建时间"
        width="170"
        prop="createdDate"
        show-overflow-tooltip
        sortable
      />
      <el-table-column
        label="备注"
        min-width="180"
        prop="remark"
        show-overflow-tooltip
      />
      <el-table-column
        label="操作"
        width="230"
        align="center"
      >
        <template v-slot="scope">
          <div class="el-link-parent">
            <el-link
              v-permission="'sys:user:edit'"
              icon="el-icon-edit"
              :underline="false"
              type="primary"
              :disabled="Number(scope.row.id) <= 2147483647"
              @click="editUser(scope.row)"
            >编辑</el-link>
            <el-link
              v-permission="'sys:user:reset-password'"
              icon="el-icon-key"
              :underline="false"
              type="warning"
              :disabled="Number(scope.row.id) <= 2147483647"
              @click="resetPassword(scope.row)"
            >重置密码</el-link>
            <el-link
              v-permission="'sys:user:delete'"
              icon="el-icon-delete"
              :underline="false"
              type="danger"
              :disabled="Number(scope.row.id) <= 2147483647"
              @click="deleteUser(scope.row)"
            >删除</el-link>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页控件 -->
    <el-pagination
      :current-page="table.page"
      :pager-count="table.pagerCount"
      :page-size="table.size"
      :page-sizes="table.pageSizes"
      :total="table.total"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />

    <!--  新增菜单弹窗  -->
    <CreateMenuDialog :create-menu-dialog.sync="createMenuDialog" :refresh-menu="load" />
  </div>
</template>

<script>
import { initMenuApi, listMenuApi } from "@/api/menu";
import { menuTreeHandler } from "@/utils/tree";
import { goudongWebAdminResource } from "@/router/modules/goudong-web-admin-router";
import { MENU_TYPE_ARRAY } from "@/constant/commons"

export default {
  name: 'MenuPage',
  components: {
    CreateMenuDialog: () => import('@/views/menu/components/CreateMenuDialog'),
    DetailMenu: () => import('@/views/menu/components/DetailMenu'),
  },
  data() {
    return {

      menuTypeArray: MENU_TYPE_ARRAY,
      filter: {
        name: undefined,
        type: undefined,
        permissionId: undefined,
        path: undefined,
      },
      filterText: undefined,
      menus: [],
      props: {
        label: 'name',
        children: 'children'
      },

      highlightCurrent: true, // 高亮显示  不让背景消失
      selectMenu: { // 当前选中的菜单
        menuFullName: ''
      },
      // menuVisible: false,
      createMenuDialog: false, // 创建菜单弹窗
      table: {
        isLoading: false,
        data: [],
        page: undefined,
        pagerCount: undefined,
        size: undefined,
        pageSizes: undefined,
        total: undefined,
        elDropdownItemClass: ['el-dropdown-item--click', undefined, undefined],
        EL_TABLE: {
          // 显示大小
          size: 'medium'
        },
      },


    }
  },
  watch: {

  },
  mounted() {
    this.load()
  },
  methods: {
    load() {

      listMenuApi(this.filter).then(data => {
        console.log("data")
        this.menus = data;
        // 将菜单中的接口过滤
        // const arr2 = menuTreeHandler(this.menus)
        // this.$store.dispatch('menu/setAllMenus', arr2);
      })
    },

    getReturnNode(node, _array, value) {
      const isPass =
        node.data &&
        node.data.name &&
        node.data.name.indexOf(value) !== -1;
      isPass ? _array.push(isPass) : "";
      if (!isPass && node.level !== 1 && node.parent) {
        this.getReturnNode(node.parent, _array, value);
      }
    },
    // 推送菜单
    initMenu() {
      const menus = [];
      console.log(goudongWebAdminResource)
      goudongWebAdminResource.filter(f => !f.hidden).forEach((item, index, arr) => {
        const obj = this.generate(item);
        menus.push(obj)
      })
      console.log(menus);

      initMenuApi(menus).then(data => {
        this.$message.success("推送成功")
      })
    },
    // 查询
    searchFunc() {
      alert("1");
    },
    resetSearchFilter() {
      this.filter = {}
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
    addMenu() { // 新增菜单
      this.createMenuDialog = true
    },

    // 修改表格大小
    changeElTableSizeCommand(val) {
      const args = val.split(",");
      const idx = Number(args[0]);
      console.log(args)
      this.table.elDropdownItemClass.map((value, index, array) => {
        if (index === idx) {
          array[index] = "el-dropdown-item--click";
        } else {
          array[index] = undefined;
        }
      })
      console.log(this.table.elDropdownItemClass)
      this.table.elDropdownItemClass[args[0]]
      this.table.EL_TABLE.size = args[1];
    },
    // 更改每页显示多少条
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
      this.table.size = val
      // this.loadPageUser()
    },
    // 修改当前页码
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`)
      this.table.page = val
      // this.loadPageUser()
    },
  }
}
</script>
<style lang="scss" scoped>
@import '@/styles/variables.scss';
.app-container-main{
  .el-header{
    padding: 20px 20px;
    border-bottom: 1px solid $borderColor;
  }
  .app-inner-container {
    min-height: calc(100vh - 126px);
    padding: 0 20px;
    aside{
      background-color: #fff;
      position: relative;
      border-right: 1px solid $borderColor;
      padding: 0 20px 0 0;
      .menu-filter-div{
        padding: 20px 0 20px 0;
      }
      .el-tree {
        .el-tree-node-span {
          display: inline-block;
          margin-left: 5px;
        }
      }
    }
  }
}

</style>
