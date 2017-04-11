#ifndef SORT_H_INCLUDED
#define SORT_H_INCLUDED
#include <math.h>
#include <iostream>
#define maxnumber 10000
using namespace std;
class fast_sort
{
    int origin[maxnumber];
    int temp[maxnumber];
    int num;
    int findpivot(int i,int j);
    int divide(int i,int j, int pivot);
    void quick_sort_help(int i,int j);
    int steps;
    void mpass(int l,int A[],int B[]);
    void merge_help(int l,int m,int n,int A[],int B[]);
    //void bucket_digit(int n);
    int maxbit();
public:
    fast_sort();
    ~fast_sort() {};
    void bubble_sort();
    void insert_sort();
    void select_sort();
    void quick_sort();
    void merge_sort();
    void shell_sort();
    void radix_sort();
};

int fast_sort::maxbit() //辅助函数，求数据的最大位数
{
    int d; //保存最大的位数
    int p;
    int maxd=0;
    for(int i = 0; i < num; ++i)
    {
        d=1;
        p=10;
        while(temp[i] >= p)
        {
            p*=10;
            ++d;
        }
        if(d>maxd) maxd=d;
    }
    return maxd;
}
void fast_sort::radix_sort() //基数排序
{
    steps=0;
    int d = maxbit();
    int * tmp = new int[num];
    int * count = new int[10]; //计数器
    int i,j,k;
    int radix = 1;
    for(i = 1; i<= d; i++) //进行d次排序
    {
        for(j = 0; j < 10; j++)
            count[j] = 0; //每次分配前清空计数器
            steps+=10;
        for(j = 0; j < num; j++)
        {
            k = (temp[j]/radix)%10; //统计每个桶中的记录数
            count[k]++;
            steps++;
        }
        for(j = 1; j < 10; j++)
            count[j] = count[j-1] + count[j]; //将tmp中的位置依次分配给每个桶
            steps+=9;
        for(j = num-1; j >= 0; j--) //将所有桶中记录依次收集到tmp中
        {
            k = (temp[j]/radix)%10;
            tmp[count[k]-1] = temp[j];
            count[k]--;
            steps++;
        }
        for(j = 0; j < num; j++) //将临时数组的内容复制到data中
            temp[j] = tmp[j];
            steps+=num;
        radix = radix*10;
    }
    delete [] tmp;
    delete [] count;
    cout<<"radix sort:"<<endl;
    for(int i=0; i<num; i++)
    {
        cout<<temp[i]<<" ";
        temp[i]=origin[i];
    }
    cout<<endl;
    cout<<"times :"<<steps<<endl;
//    cout<<"temp[] :";
//    for(int i=0; i<num; i++)
//    {
//        cout<<temp[i]<<" ";
//    }
//    cout<<endl;
}

void fast_sort::shell_sort()
{
    steps=0;
    int t,j;
    for(int d=num/2; d>0; d=d/2)
    {
        for(int i=d; i<num; i++)
        {
            steps++;
            t=temp[i];
            for(j=i-d; (j>=0)&&(temp[j]>t); j-=d)
            {
                temp[j+d]=temp[j];
            }
            temp[j+d]=t;
        }
    }
    cout<<"shell sort:"<<endl;
    for(int i=0; i<num; i++)
    {
        cout<<temp[i]<<" ";
        temp[i]=origin[i];
    }
    cout<<endl;
    cout<<"times :"<<steps<<endl;
//    cout<<"temp[] :";
//    for(int i=0; i<num; i++)
//    {
//        cout<<temp[i]<<" ";
//    }
//    cout<<endl;
}
void fast_sort::merge_help(int l,int m,int n,int A[],int B[])
{
    steps+=(n-l);
    int i=l,j=m+1,k=l;
    while(i<=m&&j<=n) B[k++]=(A[i]<=A[j])?A[i++]:A[j++];
    while(i<=m) B[k++]=A[i++];
    while(j<=n) B[k++]=A[j++];
}
void fast_sort::mpass(int l,int A[],int B[])
{
    int i;
    for(i=0; i+2*l<num; i+=2*l)
        merge_help(i,i+l-1,i+2*l-1,A,B);
    if(i+l<num)
        merge_help(i,i+l-1,num-1,A,B);
    else
    {
        for(int t=i; t<num; t++)
            B[t]=A[t];
        steps+=(num-i);
    }
}
void fast_sort::merge_sort()
{
    steps=0;
    int temp2[maxnumber];
    int l=1;
    while(l<num)
    {
        mpass(l,temp,temp2);
        l*=2;
        mpass(l,temp2,temp);
        l*=2;
    }
    cout<<"merge sort:"<<endl;
    for(int i=0; i<num; i++)
    {
        cout<<temp[i]<<" ";
        temp[i]=origin[i];
    }
    cout<<endl;
    cout<<"times :"<<steps<<endl;
//    cout<<"temp[] :";
//    for(int i=0; i<num; i++)
//    {
//        cout<<temp[i]<<" ";
//    }
//    cout<<endl;
//    cout<<"temp2[] :";
//    for(int i=0; i<num; i++)
//    {
//        cout<<temp2[i]<<" ";
//    }
//    cout<<endl;
}
void fast_sort::quick_sort()
{
    steps=0;
    quick_sort_help(0,num-1);
    cout<<"quick sort:"<<endl;
    for(int i=0; i<num; i++)
    {
        cout<<temp[i]<<" ";
        temp[i]=origin[i];
    }
    cout<<endl;
    cout<<"times :"<<steps<<endl;
//    cout<<"temp[] :";
//    for(int i=0; i<num; i++)
//    {
//        cout<<temp[i]<<" ";
//    }
//    cout<<endl;
}
void fast_sort::quick_sort_help(int i,int j)
{
    steps++;
    int pivot,k,pivot_index;
    pivot_index=findpivot(i,j);
    if(pivot_index!=-1)
    {
        pivot=temp[pivot_index];
        k=divide(i,j,pivot);
        quick_sort_help(i,k-1);
        quick_sort_help(k,j);
    }
}
int fast_sort::divide(int i,int j, int pivot)
{
    int l=i,r=j,t;
    do
    {
        steps++;
//        cout<<"l:"<<l<<" r:"<<r<<endl;
//        cout<<"do while"<<endl;
        while(temp[l]<pivot)
        {
            steps++;
            l++;
//            cout<<"l while"<<endl;
        }
        while(temp[r]>pivot)
        {
            steps++;
            r--;
//        cout<<"r while"<<endl;
        }
        if(l<r)
        {
            steps++;
            t=temp[l];
            temp[l]=temp[r];
            temp[r]=t;
//            l++;
//            r--;
        }
    }
    while(l<r);
    return l;
}
int fast_sort::findpivot(int i,int j)
{
    int first=temp[i];
    for(int k=i+1; k<=j; k++)
    {
        steps++;
        if(temp[k]>first) return k;
        else if(temp[k]<first) return i;
    }
    return -1;
}
void fast_sort::select_sort()
{
    int pos,times=0,t;
    for(int i=0; i<num; i++)
    {
        pos=i;
        for(int j=i+1; j<num; j++)
        {
            times++;
            if(temp[j]<temp[pos])
            {
                pos=j;
            }
        }
        t=temp[i];
        temp[i]=temp[pos];
        temp[pos]=t;
    }
    cout<<"select sort:"<<endl;
    for(int i=0; i<num; i++)
    {
        cout<<temp[i]<<" ";
        temp[i]=origin[i];
    }
    cout<<endl;
    cout<<"times :"<<times<<endl;
//    cout<<"temp[] :";
//    for(int i=0; i<num; i++)
//    {
//        cout<<temp[i]<<" ";
//    }
//    cout<<endl;
}
void fast_sort::insert_sort()
{
    int j,times=0,t;
    for(int i=0; i<num; i++)
    {
        j=i;
        while(j>=0&&temp[j]<temp[j-1])
        {
            times++;
            t=temp[j];
            temp[j]=temp[j-1];
            temp[j-1]=t;
            j--;
        }
    }
    cout<<"insert sort:"<<endl;
    for(int i=0; i<num; i++)
    {
        cout<<temp[i]<<" ";
        temp[i]=origin[i];
    }
    cout<<endl;
    cout<<"times :"<<times<<endl;
//    cout<<"temp[] :";
//    for(int i=0; i<num; i++)
//    {
//        cout<<temp[i]<<" ";
//    }
//    cout<<endl;
}
void fast_sort::bubble_sort()
{
    int times=0,finish=1,t;
    for(int i=0; i<num; i++)
    {
        if(finish)
        {
            finish=0;
            for(int j=num-1; j>i; j--)
            {
                times++;
                if(temp[j-1]>temp[j])
                {
                    finish=1;
                    t=temp[j];
                    temp[j]=temp[j-1];
                    temp[j-1]=t;
                }
            }
        }

    }
    cout<<"bubble sort:"<<endl;
    for(int i=0; i<num; i++)
    {
        cout<<temp[i]<<" ";
        temp[i]=origin[i];
    }
    cout<<endl;
    cout<<"times :"<<times<<endl;
//    cout<<"temp[] :";
//    for(int i=0; i<num; i++)
//    {
//        cout<<temp[i]<<" ";
//    }
//    cout<<endl;
}
fast_sort::fast_sort()
{
    cout<<"input the number of the data:";
    cin>>num;
    for(int i=0; i<num; i++)
    {
        cout<<"input the nodes #"<<i+1<<":";
        cin>>origin[i];
        temp[i]=origin[i];
    }
}
#endif // SORT_H_INCLUDED
