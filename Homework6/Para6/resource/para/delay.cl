#pragma OPENCL EXTENSION cl_khr_byte_addressable_store : enable

__kernel void Delay(const int width, const int height,
         __global uchar* latest, __global uchar* previous,
         __global uchar* output, const float p){

           int x = get_global_id(0);
           int y = get_global_id(1);
           previous[(y*width+x)*3] = output[(y*width+x)*4  ] =
             (uchar)(latest[(y*width+x)*3  ]*p + (1-p)*previous[(y*width+x)*3  ]);
           previous[(y*width+x)*3+1] = output[(y*width+x)*4+1] =
             (uchar)(latest[(y*width+x)*3+1]*p + (1-p)*previous[(y*width+x)*3+1]);
           previous[(y*width+x)*3+2] = output[(y*width+x)*4+2] =
             (uchar)(latest[(y*width+x)*3+2]*p + (1-p)*previous[(y*width+x)*3+2]);
           output[(y*width+x)*4+3] = 0xff;




}
