#pragma OPENCL EXTENSION cl_khr_byte_addressable_store : enable
// OpenCL Kernel Function
int addr(const int width, const int height, int x, int y){
    if(y<0){y=0;}
    if(height-1<y){y=height-1;}
    if(x<0){x=0;}
    if(width-1<x){x=width-1;}
    return (y*width*3+x*3);
}
float filter1(__global const uchar* in, const int width, const int height,
              const int lx,const int ly, const int shift){
    return (
            in[addr(width, height, lx-1, ly-1)+shift] * -1+
            in[addr(width, height, lx  , ly-1)+shift] * 0+
            in[addr(width, height, lx+1, ly-1)+shift] * 1+
            
            in[addr(width, height, lx-1, ly)+shift] * -2+
            in[addr(width, height, lx  , ly)+shift] * 0+
            in[addr(width, height, lx+1, ly)+shift] * 2+
            
            in[addr(width, height, lx-1, ly+1)+shift] * -1+
            in[addr(width, height, lx  , ly+1)+shift] * 0+
            in[addr(width, height, lx+1, ly+1)+shift] * 1
            );
}

float filter2(__global const uchar* in, const int width, const int height,
              const int lx,const int ly, const int shift){
    return (
            in[addr(width, height, lx-1, ly-1)+shift] * -1+
            in[addr(width, height, lx  , ly-1)+shift] * -2+
            in[addr(width, height, lx+1, ly-1)+shift] * -1+
            
            in[addr(width, height, lx-1, ly)+shift] * 0+
            in[addr(width, height, lx  , ly)+shift] * 0+
            in[addr(width, height, lx+1, ly)+shift] * 0+
            
            in[addr(width, height, lx-1, ly+1)+shift] * 1+
            in[addr(width, height, lx  , ly+1)+shift] * 2+
            in[addr(width, height, lx+1, ly+1)+shift] * 1
            );
}

float bound(const float in){
    if(in<0) return 0;
    if(in>255) return 255.0f;
    return in;
}

__kernel void Filter2(const int width, const int height,
                      __global const uchar* in,
                      __global uchar *outb,
                      const float scale) {
    // get index of global data array
    int lx = get_global_id(0);
    int ly = get_global_id(1);
    /*
     // bound check (equivalent to the limit on a 'for' loop for standard/serial C code
     if (lx > width || ly >height)  {
     return;
     }
     */
    float samp = scale/1600;
    //int add = addr(width,height,lx,ly);
    int oadd = (ly*width+lx)*4;
    
    float f11;
    f11 = filter1(in,width,height,lx,ly,0);
    f11 *= f11;
    float f12;
    f12= filter2(in,width,height,lx,ly,0);
    f12 *= f12;
    outb[oadd  ]= bound((f11+f12)*samp);
    f11 = filter1(in,width,height,lx,ly,1);
    f11 *= f11;
    f12 = filter2(in,width,height,lx,ly,1);
    f12 *= f12;
    outb[oadd+1]= bound((f11+f12)*samp);
    f11 = filter1(in,width,height,lx,ly,2);
    f11 *= f11;
    f12 = filter2(in,width,height,lx,ly,2);
    f12 *= f12;
    outb[oadd+2]= bound((f11+f12)*samp);

    
    outb[oadd+3]= 255;
}
