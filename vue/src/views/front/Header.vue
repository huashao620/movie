<template>
  <el-carousel :interval="2000">
    <el-carousel-item style="object-fit: cover">
      <img src="@/assets/images/lunbo1.png" style="width: 100%; height: 100%;"/>
    </el-carousel-item>
    <el-carousel-item>
      <img src="@/assets/images/lunbo2.png" style="width: 100%; height: 100%; "/>
    </el-carousel-item>
    <el-carousel-item>
      <img src="@/assets/images/lunbo3.png" style="width: 100%;  height: 100%;"/>
    </el-carousel-item>
  </el-carousel>
</template>

<script>
export default {
  name: "Header.vue"
}
</script>

<style scoped>

</style>
