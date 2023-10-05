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
        <el-button class="el-button--small" icon="el-icon-s-promotion" type="primary"
                   @click="switchOpenDrag"
        >{{ switchButtonName }}
        </el-button>
        <el-button class="el-button--small" type="primary" @click="openTable(true)">展开</el-button>
        <el-button class="el-button--small" type="primary" @click="openTable(false)">折叠</el-button>
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
      :key="dataKey"
      :expand-row-keys="table.expandKeys"
      row-key="id"
      style="width: 100%"
      :header-cell-style="{background:'#FAFAFA', color:'#000', height: '30px',}"
      :header-row-class-name="table.EL_TABLE.size"
      :size="table.EL_TABLE.size"
    >
      <el-table-column
        type="index"
        label="拖拽"
        width="60"
        align="center"
        :class-name="switchClassName"
      >
        <i class="el-icon-s-promotion handle" />
      </el-table-column>
      <el-table-column
        label="名称"
        min-width="150"
        prop="name"
        show-overflow-tooltip
      />
      <el-table-column
        label="权限标识"
        min-width="150"
        prop="permissionId"
        show-overflow-tooltip
      >
        <template v-slot="scope">
          <span class="copy_column_class el-icon-document-copy" @click="copyPermissionId(scope.row)">
            <span style="width: 5px; display: inline-block;"></span>{{scope.row.permissionId}}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        label="类型"
        prop="type"
        max-width="50"
      >
        <template v-slot="scope">
          <span v-if="scope.row.type === 1">菜单</span>
          <span v-else-if="scope.row.type === 2">按钮</span>
          <span v-else-if="scope.row.type === 3">接口</span>
          <span v-else>未知类型</span>
        </template>
      </el-table-column>
      <el-table-column
        label="资源路径"
        width="170"
        prop="path"
        show-overflow-tooltip
      >
        <template v-slot="scope">
          <span class="copy_column_class el-icon-document-copy" @click="copyPath(scope.row)">
            <span style="width: 5px; display: inline-block;"></span>{{scope.row.path}}
          </span>
        </template>
      </el-table-column>
      <el-table-column
        label="请求方式"
        width="100"
        prop="method"
        align="left"
        show-overflow-tooltip
      >
        <template v-slot="scope">
          <span v-for="item in getMethod(scope.row)" :key="item">
            <el-tag size="small" :class="item" class="http_method_tag">{{ item }}</el-tag> <br>
          </span>
        </template>
      </el-table-column>
      <el-table-column
        label="排序"
        width="50"
        prop="sortNum"
      />
      <el-table-column
        label="隐藏"
        width="50"
        prop="hide"
      >
        <template v-slot="scope">
          <span>{{ scope.row.hide ? '隐藏' : '可见' }}</span>
        </template>
    </el-table-column>
      <el-table-column
        label="创建时间"
        width="150"
        prop="createdDate"
        show-overflow-tooltip
      />
      <el-table-column
        label="备注"
        width="100"
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
              v-permission="'sys:menu:edit'"
              icon="el-icon-edit"
              :underline="false"
              type="primary"
              :disabled="Number(scope.row.id) <= 2147483647"
              @click="updateMenu(scope.row)"
            >编辑</el-link>
            <el-link
              v-permission="'sys:menu:delete'"
              icon="el-icon-delete"
              :underline="false"
              type="danger"
              :disabled="Number(scope.row.id) <= 2147483647"
              @click="deleteMenu(scope.row)"
            >删除</el-link>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <!--  新增菜单弹窗  -->
    <CreateMenuDialog :create-menu-dialog.sync="createMenuDialog" :refresh-menu="load" />
    <!--  修改菜单弹窗  -->
    <UpdateMenuDialog :update-menu-dialog.sync="updateMenuDialog" :update-menu-data="updateMenuData" :refresh-menu="load" />
  </div>
</template>

<script>
import { changeSortNumApi, deleteMenuByIdApi, initMenuApi, listMenuApi } from "@/api/menu";
import { menuTreeHandler } from "@/utils/tree";
import { goudongWebAdminResource } from "@/router/modules/goudong-web-admin-router";
import { MENU_TYPE_ARRAY } from "@/constant/commons"
import Sortable from 'sortablejs';
import { deleteRoleByIdsApi } from "@/api/role";
import { copyText } from "@/utils/StringUtil";

export default {
  name: 'MenuPage',
  components: {
    CreateMenuDialog: () => import('@/views/menu/components/CreateMenuDialog'),
    UpdateMenuDialog: () => import('@/views/menu/components/UpdateMenuDialog'),
    DetailMenu: () => import('@/views/menu/components/DetailMenu'),
  },
  data() {
    return {
      menuTypeArray: MENU_TYPE_ARRAY,
      dataKey: new Date().getTime(),
      filter: {
        name: undefined,
        type: undefined,
        permissionId: undefined,
        path: undefined,
      },
      props: {
        label: 'name',
        children: 'children'
      },
      // menuVisible: false,
      createMenuDialog: false, // 创建菜单弹窗
      updateMenuDialog: false, // 创建菜单弹窗
      updateMenuData: undefined, // 修改菜单得数据
      table: {
        expandKeys: undefined,
        isLoading: false,
        rawData: [], // 初始数据
        data: [], // 渲染的数据
        activeRows: [], // 转换为列表(一维数组)的数据
        elDropdownItemClass: ['el-dropdown-item--click', undefined, undefined],
        EL_TABLE: {
          // 显示大小
          size: 'medium'
        },
        sort_able: undefined,
      },
      openDrag: false, // 开启拖拽
      switchButtonName: '开启拖拽', // 拖拽的按钮文字
      switchClassName: 'close-switch-drag', // 拖拽列得class
    }
  },
  watch: {

  },
  mounted() {
    this.load()
  },
  methods: {
    openTable(flag) { // 展开/折叠
      this.$nextTick(() => {
        this.handleArr(this.table.data, flag);
      });

    },
    handleArr(arr, flag) {
      arr.forEach(i => {
        this.$refs.table.toggleRowExpansion(i, flag);
        if (i.children) {
          this.handleArr(i.children);
        }
      });
    },
    // 切换拖拽
    switchOpenDrag() {
      // 状态取反
      this.openDrag = !this.openDrag;
      this.switchButtonName = this.openDrag ? "关闭拖拽" : "开启拖拽"
      if (!this.openDrag) {
        this.cancelRowDrop()
        this.switchClassName = "close-switch-drag"
      } else {
        this.rowDrop()
        this.switchClassName = "open-switch-drag"

      }
    },
    load() {
      this.table.isLoading = true;
      this.dataKey = new Date().getTime();
      listMenuApi({}).then(data => {
        console.log("data", data.records)
        this.table.data = data.records;
        this.table.rawData = data.records;
        this.$store.dispatch('menu/setAllMenus', data.records)
        // this.table.expandKeys = data.records.map(m => m.id);

      }).finally(() => {
        this.table.isLoading = false
      })
    },
    getMethod(row) {
      if (row.method) {
        return JSON.parse(row.method)
      }
      return []
    },
    // 将树数据转化为平铺数据
    treeToTile(treeData, childKey = 'children') {
      const arr = []
      const expanded = data => {
        if (data && data.length > 0) {
          data.filter(d => d).forEach(e => {
            arr.push(e)
            expanded(e[childKey] || [])
          })
        }
      }
      expanded(treeData)
      return arr
    },
    // 行拖拽
    rowDrop() {
      const tbody = document.querySelector('.el-table__body-wrapper tbody')
      const _this = this
      this.sort_able = Sortable.create(tbody, {
        //  指定父元素下可被拖拽的子元素
        handle: ".handle",
        onMove: () => {
          _this.table.activeRows = this.treeToTile(_this.table.data) // 把树形的结构转为列表再进行拖拽
        },
        onEnd({ newIndex, oldIndex }) {
          // 1. 校验只能同级
          if (_this.table.activeRows[newIndex].parentId !== _this.table.activeRows[oldIndex].parentId) {
            _this.$message.error("只能同级进行移动");
            _this.dataKey=new Date().getTime()
            _this.switchOpenDrag()
            return;
          } else {
            const beforeId = _this.table.activeRows[oldIndex].id
            const afterId = _this.table.activeRows[newIndex].id
            console.log(oldIndex, newIndex);
            console.log(beforeId, afterId);
            const currRow = _this.table.activeRows.splice(oldIndex, 1)[0]
            _this.table.activeRows.splice(newIndex, 0, currRow)
            changeSortNumApi({beforeId: beforeId, afterId: afterId}).then(data => {
              _this.load();
            })
          }
        }
      })
    },
    // 取消拖拽
    cancelRowDrop() {
      if (this.sort_able) {
        this.openDrag = false;
        this.switchButtonName = this.openDrag ? "关闭拖拽" : "开启拖拽"
        this.sort_able.destroy()
      }
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
      // 筛选
      const arr = []
      this.table.rawData.filter((item) => {
        return this.menuFilter(item, arr);
      })
      this.table.data = arr
      console.log(JSON.stringify(arr));
    },
    menuFilter(menu, arr) {
      // 本节点满足条件，直接添加到数组arr。
      let flag = true
      // 菜单名
      flag = flag && (!this.filter.name || (this.filter.name && menu.name.indexOf(this.filter.name) !== -1))
      // 菜单类型
      flag = flag && (!this.filter.type || (this.filter.type && menu.type === this.filter.type))
      // 权限标识
      flag = flag && (!this.filter.permissionId || (this.filter.permissionId && menu.permissionId.indexOf(this.filter.permissionId) !== -1))
      // 资源路径
      flag = flag && (!this.filter.path || (this.filter.path && menu.path.indexOf(this.filter.path) !== -1))
      // 所有条件满足，添加到集合
      if (flag) {
        arr.push(menu)
        return true;
      }
      // 本节点不满足条件，继续遍历子节点
      if (menu.children) {
        menu.children.filter((item) => {
          return this.menuFilter(item, arr);
        })
      }
      return false;
    },
    resetSearchFilter() {
      this.filter = {}
      // 重新加载
      this.load();
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
    updateMenu(row) { // 修改菜单
      this.updateMenuData = row
      this.updateMenuDialog = true
      console.log("row", this.updateMenuData)
    },
    deleteMenu(row) { // 删除菜单
      this.$confirm(`此操作将永久删除”${row.name}“整个菜单, 是否继续?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMenuByIdApi(row.id).then(response => {
          this.$message.success("删除成功")
          this.load()
        })
      }).catch(() => {
        this.$message.info("已取消删除");
      })
    },
    copyPermissionId(row) { // 拷贝权限标识
      copyText(row.permissionId, this)
    },
    copyPath(row) { // 拷贝资源路径
      copyText(row.path, this)
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
  }
}
</script>
<style lang="scss" scoped>
@import '@/styles/variables.scss';

::v-deep .open-switch-drag{
  cursor: move;
  .cell {

  }
}
::v-deep .close-switch-drag{
  cursor: not-allowed;
  .cell {
    opacity: 0.4;
  }
}
.http_method_tag{
  width: 70px;
  text-align: center;
}
::v-deep .GET{
  background-color: $GET;
  color: #fff
}
::v-deep .POST{
  background-color: $POST;
  color: #fff
}
::v-deep .PUT{
  background-color: $PUT;
  color: #fff
}
::v-deep .DELETE{
  background-color: $DELETE;
  color: #fff
}
::v-deep .PATCH{
  background-color: $PATCH;
  color: #fff
}
::v-deep .OPTIONS{
  background-color: $OPTIONS;
  color: #fff
}
::v-deep .TRACE{
  background-color: $TRACE;
  color: #fff
}
::v-deep .HEAD{
  background-color: $HEAD;
  color: #fff
}
</style>
