<!--用户名模糊下拉分页-->
<template>
  <div class="username-select-container">
    <el-select
      v-model="selectedUser"
      v-load-more="loadMore"
      style="width: 230px;"
      :loading="loading"
      clearable
      placeholder="请输入用户名"
      @change="change"
      @clear="clear"
    >
      <el-option
        v-for="item in users"
        :key="item.value"
        :label="item.label"
        :value="{value: item.value, label: item.label}"
      />
    </el-select>
  </div>
</template>

<script>

import { dropDownUserApi } from '@/api/dropDown';

export default {
  data() {
    return {
      selectedUser: undefined,
      loading: false,
      users: [],
      page: 1,
      size: 10,
      totalPage: undefined, // 下拉总页码
    }
  },
  mounted() {
    // 优先加载表格数据
    this.loadUser()
  },
  methods: {
    // 加载用户
    loadUser() {
      this.loading = true
      const page = { page: this.page, size: this.size }
      dropDownUserApi(page).then(data => {
        this.totalPage = Number(data.totalPage)
        const content = data.content
        content.forEach(user => {
          this.users.push({ value: user.id, label: user.name })
        })
      }).catch(err => {
        console.warn('err', err)
      }).finally(() => {
        this.loading = false
      })
    },
    // 修改选项
    change(selectedUser) {
      console.log(selectedUser)
      // 给父组件传递值
      this.$emit('getSelectedUser', selectedUser)
    },
    // 继续加载
    loadMore: function() {
      // 总页数大于当前页，请求下一页数据
      if (this.totalPage > this.page) {
        this.page += 1;
        this.loadUser();
      }
    },
    // 父组件调用清除组件值 this.$refs.userSelectRef.clear();
    clear() {
      // 去掉框中的值
      this.selectedUser = undefined;
      // 给父组件传递值
      this.$emit('getSelectedUser', this.selectedUser)
    },
  }
}
</script>

<style scoped>
  .username-select-container{
    display: inline-block;
  }
</style>
