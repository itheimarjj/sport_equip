<script setup>
import { ElButton, ElIcon, ElMessage } from 'element-plus';
import { Plus, Minus } from '@element-plus/icons-vue';

import { ref, onMounted, watch } from 'vue'
import axios from 'axios'
import instance from '@/utils/request';

import useUserInfoStore from '@/stores/userInfo.js'
const userInfoStore = useUserInfoStore();

//器材列表数据模型
const equipment = ref([]);
const equipnames = ref([]);
const equipmodels = ref([]);
//分页条数据模型
const pageNum = ref(1)//当前页
const total = ref(40)//总条数
const pageSize = ref(5)//每页条数
const searchCount = ref(0); // 用于追踪方法调用次数

// 工具函数：生成自定义格式时间
// 获取当前时间
let now = ref();

// 手动格式化为 yyyy-MM-dd HH:mm:ss
const formatDate = (date) => {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份从0开始
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
};

// 输出：2023-10-05 15:30:45

//借用器材
const lentEquip = async () => {
    now = new Date();
    const status = await instance.put("/equip/lentequip", {
        userid: userInfoStore.info.userid,                            //借用人
        equipment: Addform.value.name,//器材名字
        model: Addform.value.model,
        num: Addform.value.num,//器材数量
        status: "借用",
        borrowDate: formatDate(now)
    }, {
        headers: {
            // 添加 Authorization 头（替换 your_token 为实际令牌）
            'Authorization': tokenStore.token,
        }
    }
    );
    if (status.code === 200) {
        ElMessage({
            message: '已发送预约请求，请前往教务处领取',
            type: 'success',
            plain: true,
        })
    } else {
        ElMessage.warning("借用失败，请稍后再试")
    }
    dialogVisibleAdd.value = false;

    Addform.value.num = 1;
    if (searchCount.value > 0) {
        selectbydynamic();
    } else {
        fetchEquipment();
    }

}
//归还器材
const backequip = async () => {
    now = new Date();
    const status = await instance.put("/equip/backequip", {
        userid: userInfoStore.info.userid, //借用人
        equipment: Addform.value.name,//器材名字
        model: Addform.value.model,
        num: Addform.value.num,//器材数量
        status: "归还",
        returnDate: formatDate(now)
    }, {
        headers: { 'Authorization': tokenStore.token },
    }
    );
    if (status.code === 200) {
        ElMessage({
            message: '已发送归还请求，请在30分钟内前往教务处归还器材',
            type: 'success',
            plain: true,
        })
    } else {
        ElMessage.warning("归还失败，请稍后再试")
    }
    dialogVisibleDelete.value = false;

    Addform.value.num = 1;
    if (searchCount.value > 0) {
        selectbydynamic();
    } else {
        fetchEquipment();
    }

}

//刷新页面就触发
const fetchEquipment = async () => {
    try {
        const response = await axios.get('http://localhost:8080/equip/pagelist', {
            params: {
                page: pageNum.value,
                pageSize: pageSize.value // 每页数量
            }
        });
        searchCount.value = 0;//刷新页面的时候归零



        if (response.data && response.data.items) {
            equipment.value = response.data.items; // 通过 .value 更新响应式数据
            total.value = response.data.total;//更新总条数
        } else {
            console.warn("接口未返回 items 字段，实际数据结构:", response.data);
            equipment.value = []; // 安全回退
        }

    } catch (error) {
        console.error("Failed to fetch equipment list:", error);
    }
};

//获取所有
const fetchEquipmentall = async () => {
    try {
        const responseall = await axios.get('http://localhost:8080/equip/list');
        //名称去重
        equipnames.value = responseall.data.filter(
            (item, index, self) =>
                index === self.findIndex((t) => t.name === item.name)
        );
        //品牌去重
        equipmodels.value = responseall.data.filter(
            (item, index, self) =>
                index === self.findIndex((t) => t.model === item.model)
        );
    } catch (error) {
        console.error("请求失败:", error);
    }
};



//根据名称或类型查询
const selectbydynamic = async () => {
    try {
        if (!selectedEquipment.value && !selectedType.value) {
            ElMessage.warning("请选择器材名称或类型！");
            return;
        }
        const responsebyname = await axios.get('http://localhost:8080/equip/selectByNameOrModel', {
            params: {
                page: pageNum.value,
                pageSize: pageSize.value, // 每页数量
                equipname: selectedEquipment.value,//绑定名称
                equipmodel: selectedType.value

            }
        });

        searchCount.value++;//每次触发时调用

        if (responsebyname.data && responsebyname.data.items) {
            equipment.value = responsebyname.data.items; // 通过 .value 更新响应式数据
            total.value = responsebyname.data.total;//更新总条数
        } else {
            console.warn("接口未返回 items 字段，实际数据结构:", responsebyname.data);
            equipment.value = []; // 安全回退
        }
    } catch (error) {
        console.error("请求失败:", error);
    }
};



onMounted(fetchEquipment);
onMounted(fetchEquipmentall);

//当每页条数发生了变化，调用此函数
const onSizeChange = (size) => {
    pageSize.value = size
    if (searchCount.value > 0) {
        selectbydynamic();
    } else {
        fetchEquipment();
    }



}
//当前页码发生变化，调用此函数
const onCurrentChange = (num) => {
    pageNum.value = num
    if (searchCount.value > 0) {
        selectbydynamic();
    } else {
        fetchEquipment();
    }
}
//重置时调用的方法
const reset = () => {
    pageNum.value = 1//当前页
    pageSize.value = 5//每页条数
    fetchEquipment();
    selectedEquipment.value = ''; selectedType.value = ''

}


import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'


//导入token
import { useTokenStore } from '@/stores/token.js';

const tokenStore = useTokenStore();


//器材名称和类型变量
const selectedEquipment = ref('');
const selectedType = ref(''); // 初始值为空字符串

//对话框设置
const currentRow = ref({}) // 存储当前操作的行数据
const Addform = ref({ name: '', model: '', num: 1 })
//对话框状态
const dialogVisibleAdd = ref(false);
const dialogVisibleDelete = ref(false);
//对话框方法
const handleAdd = (row) => {
    currentRow.value = row        // 保存当前行数据
    if (currentRow.value.stockQuantity <= 0) {
        ElMessage.warning("该品牌的器材库存为空，请选择其他品牌或其他器材");
        return;
    }
    //  Addform.name = row.name       // 设置表单值
    dialogVisibleAdd.value = true // 显示对话框
};

const handleDelete = (row) => {
    currentRow.value = row        // 保存当前行数据
    //  Addform.name = row.name       // 设置表单值
    dialogVisibleDelete.value = true // 显示对话框
};
// 监听 currentRow 的 name 属性变化
watch(
    () => currentRow.value.name,
    (newName) => {
        Addform.value.name = newName // 自动同步到 Addform.name
    },
    { immediate: true } // 立即执行一次，初始化时生效
)

// 监听 currentRow 的 model 属性变化
watch(
    () => currentRow.value.model,
    (newModel) => {
        Addform.value.model = newModel
    },
    { immediate: true } // 立即执行一次，初始化时生效
)

</script>


<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>借用与归还</span>
                <div class="extra">

                </div>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="器材分类：">
                <el-select placeholder="请选择" v-model="selectedEquipment">
                    <el-option v-for="equip in equipnames" :key="equip.id" :label="equip.name" :value="equip.name">
                    </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="器材品牌：">
                <el-select placeholder="请选择" v-model="selectedType">
                    <el-option v-for="equip in equipmodels" :key="equip.id" :label="equip.model" :value="equip.model">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="selectbydynamic">搜索</el-button>
                <el-button @click="reset">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 器材列表 -->
        <el-table :data="equipment" style="width: 100%">
            <el-table-column label="器材名称" width="400" prop="name"></el-table-column>
            <el-table-column label="型号" prop="model"></el-table-column>
            <el-table-column label="库存" prop="stockQuantity"> </el-table-column>
            <el-table-column label="入库时间" prop="entryDate"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button type="primary" @click="handleAdd(row)" circle>
                        <el-icon>
                            <Plus />
                        </el-icon>
                    </el-button>
                    <el-button type="danger" @click="handleDelete(row)" circle>
                        <el-icon>
                            <Minus />
                        </el-icon>
                    </el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
        <!-- 分页条 -->
        <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3, 5, 10, 15]"
            layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange"
            @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
    </el-card>
    <!-- 借用器材对话框 -->
    <el-dialog v-model="dialogVisibleAdd" title="借用器材" width="500">
        <el-form :model="Addform">
            <el-form-item label="器材名称:" :label-width="formLabelWidth">
                <el-input :readonly="true" :placeholder="currentRow.name" class="custom-placeholder" />
            </el-form-item>
            <el-form-item label="品牌" :label-width="formLabelWidth">
                <el-input :readonly="true" :placeholder="currentRow.model" class="custom-placeholder">
                </el-input>
            </el-form-item>
            <el-form-item label="请选择数量:" :label-width="formLabelWidth">
                <el-input v-model="Addform.num" type="number" min="1" :max="currentRow.stockQuantity" step="1"
                    onkeypress="return event.charCode >= 49" class="custom-placeholder" />
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="dialogVisibleAdd = false, Addform.num = 1">取消</el-button>
                <el-button type="primary" @click="lentEquip">
                    确认
                </el-button>
            </div>
        </template>
    </el-dialog>
    <!-- 归还器材对话框 -->
    <el-dialog v-model="dialogVisibleDelete" title="归还器材" width="500">
        <el-form :model="Addform">
            <el-form-item label="器材名称:" :label-width="formLabelWidth">
                <el-input :readonly="true" :placeholder="currentRow.name" class="custom-placeholder" />
            </el-form-item>
            <el-form-item label="品牌" :label-width="formLabelWidth">
                <el-input :readonly="true" :placeholder="currentRow.model" class="custom-placeholder">
                </el-input>
            </el-form-item>
            <el-form-item label="请选择数量:" :label-width="formLabelWidth">
                <el-input v-model="Addform.num" type="number" min="1" step="1" onkeypress="return event.charCode >= 49"
                    class="custom-placeholder" />
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="dialogVisibleDelete = false, Addform.num = 1">取消</el-button>
                <el-button type="primary" @click="backequip">
                    确认
                </el-button>
            </div>
        </template>
    </el-dialog>
</template>
<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    /* 隐藏右上角关闭按钮 */

}

:deep(.custom-placeholder .el-input__inner::placeholder) {
    font-style: bold;
    /* 斜体 */
    color: black;
    /* 字体颜色 */
    font-size: 14px;
    /* 字号 */
    font-weight: 500;
    /* 字重 */
    opacity: 0.8;
    /* 透明度 */
}
</style>