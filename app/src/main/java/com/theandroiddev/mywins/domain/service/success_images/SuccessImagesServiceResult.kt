package com.theandroiddev.mywins.domain.service.success_images

sealed class SuccessImagesServiceResult {

    data class SuccessImages(val successImages: List<SuccessImagesServiceModel>) :
            SuccessImagesServiceResult()

    class Error() : SuccessImagesServiceResult()
}
