package com.theandroiddev.mywins.domain.service.success_images

sealed class SuccessImagesServiceArgument {

    data class SuccessImages(val successImages: List<SuccessImagesServiceModel>, val successId: Long?) :
            SuccessImagesServiceArgument()

}