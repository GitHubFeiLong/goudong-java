<!--角色名模糊下拉分页-->
<template>
  <div class="role-select-container">
    <el-select
      v-model="selectedRoles"
      v-load-more="loadMore"
      :loading="loading"
      clearable
      filterable
      :multiple="roleMultiple === true"
      placeholder="请输入角色名称"
      style="width: 230px;"
      @change="change"
    >
      <el-option
        v-for="item in roles"
        :key="item.value"
        :label="item.label"
        :value="{value: item.value, label: item.label}"
      />
    </el-select>
  </div>
</template>

<script>

import { dropDownRoleApi } from "@/api/dropDown";

export default {
  name: 'RoleSelect',
  props: {
    // 角色是否是多选
    roleMultiple: {
      type: Boolean,
      required: false,
      default() {
        return false
      }
    },
    // 默认选中
    defaultRoles: {
      type: Array,
      required: false,
      default() {
        return []
      }
    },
  },
  data() {
    return {
      selectedRoles: [], // 选中的角色id集合
      loading: false,
      roles: [],
      page: 1,
      size: 10,
      totalPage: undefined, // 下拉总页码
    }
  },
  watch: {
    // 监听
    defaultRoles() {
      console.log("defaultRoles", defaultRoles)
      this.selectedRoles = this.defaultRoles
    },
  },
  mounted() {
    this.loadRole()
  },
  methods: {
    loadRole() {
      this.loading = true
      const page = { page: this.page, size: this.size }
      dropDownRoleApi(page).then(data => {
        this.totalPage = Number(data.totalPage)
        const content = data.content
        if (content && content.length > 0) {
          content.forEach((role, index, arr) => {
            this.roles.push({ value: role.id, label: role.name })
          })
        }
        // 第一次时请求接口数据还未过来，默认值设置不生效，所以这里再次设置下。
        this.selectedRoles = this.defaultRoles
      }).catch(err => {
        console.warn('err', err)
      }).finally(() => {
        this.loading = false
      })
    },
    change(selectedRoles) {
      console.log("change", selectedRoles)
      // 给父组件传递值
      this.$emit('getSelectRoles', selectedRoles)
    },
    // 下拉滚动分页
    loadMore: function() {
      // 总页数大于当前页，请求下一页数据
      if (this.totalPage > this.page) {
        this.page += 1;
        this.loadRole();
      }
    },
    // 清除已选择的
    reset() {
      this.selectedRoles = []
    }
  }
}
</script>

<style scoped>
  .role-select-container{
    display: inline-block;
  }
</style>
