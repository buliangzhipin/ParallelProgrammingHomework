#pragma OPENCL EXTENSION cl_khr_byte_addressable_store : enable

__kernel void Sum3(__constant float* a,__constant float* b,
                   __constant float* c,__global float* d){
    int i = get_global_id(0);
    d[i] = a[i] + b[i] + c[i];
}
