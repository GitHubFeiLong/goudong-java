<!--修改用户的弹框-->
<template>
  <el-dialog title="编辑用户" width="600px" :visible.sync="visible" @close="close">
    <el-form ref="editUser" :model="user" :rules="rules" label-width="80px">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="user.username" :disabled="true" />
      </el-form-item>
      <el-form-item label="角色" prop="roles">
        <RoleSelect :role-multiple="true" :default-roles="user.roles" @getSelectRoles="getSelectRoles" />
      </el-form-item>
      <el-form-item label="有效日期" prop="validTime">
        <div class="block">
          <el-date-picker
            v-model="user.validTime"
            type="datetime"
            placeholder="选择日期时间"
            value-format="yyyy-MM-dd HH:mm:ss"
          />
        </div>
      </el-form-item>
      <el-form-item label="激活" prop="enabled">
        <el-switch
          v-model="user.enabled"
          :disabled="permissionDisabled('sys:user:enable')"
          :active-value="true"
          :inactive-value="false"
        />
      </el-form-item>
      <el-form-item label="锁定" prop="locked">
        <el-switch
          v-model="user.locked"
          :disabled="permissionDisabled('sys:user:lock')"
          :active-value="true"
          :inactive-value="false"
          active-color="#F56C6C"
          inactive-color="#C0CCDA"
        />
      </el-form-item>
      <el-form-item label="备注" prop="avatar">
        <el-input v-model="user.remark" clearable />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="close">取 消</el-button>
      <el-button type="primary" @click="submitForm('editUser')">确 定</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { simpleUpdateUserApi } from '@/api/user'
import { Message } from "element-ui"

export default {
  name: 'EditUserDialog',
  components: {
    RoleSelect: () => import('@/components/User/RoleSelect')
  },
  props: {
    // 弹框
    editUserDialog: {
      required: true,
      type: Boolean,
      default: function() {
        return false
      }
    },
    editUserInfo: { required: false,
      type: Object,
      default: function() {
        return {}
      }
    }
  },
  data() {
    return {
      visible: false,
      user: {
        id: '',
        username: '',
        roles: [],
        validTime: new Date(),
        remark: '',
      },
      rules: {
        username: [
          { required: true },
        ],
        roles: [
          { type: 'array', required: true, message: '请至少选择一个角色', trigger: 'blur' }
        ],
        validTime: [
          { required: true, message: '请选择用户有效日期', trigger: 'change' }
        ],
      },
    };
  },
  watch: {
    // 监听 editUserDialog
    editUserDialog() {
      this.visible = this.editUserDialog;
      if (this.visible) {
        this.user = {
           ...this.editUserInfo
        }
        // 修改 下拉选格式
        this.user.roles = this.editUserInfo.roles.map(item => {return { value: item.id, label: item.name}})
      } else {
        this.user = {};
      }
    },
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let userReq = {
            id: this.user.id,
            roleIds: this.user.roles.map(item => item.value),
            validTime: this.user.validTime,
            enabled: this.user.enabled,
            locked: this.user.locked,
            remark: this.user.remark,
          }
          simpleUpdateUserApi(userReq).then(data => {
            console.log(data)
            Message({
              message: '保存成功',
              type: 'success',
            })
            // 关闭弹框
            this.close();
            // 刷新列表
            this.$parent.loadPageUser();
          })
        } else {
          return false;
        }
      });
    },
    // 获取子组件的角色
    getSelectRoles(selectedRoles) {
      this.user.roles = selectedRoles;
    },
    close() {
      // 表单清空
      this.$refs.editUser.resetFields();
      this.$emit("closeEditUserDialog", false)
    },
  },
}
</script>
<style>
/*动态显示和隐藏头像上传*/
.hidden{
  display: none;
}
</style>
