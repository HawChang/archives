// stdafx.h : 标准系统包含文件的包含文件，
// 或是经常使用但不常更改的
// 特定于项目的包含文件
//

#pragma once

#include "targetver.h"

#include <stdio.h>
#include <tchar.h>
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
public:
    fast_sort();
    ~fast_sort() {};
    void bubble_sort();
    void insert_sort();
    void select_sort();
    void quick_sort();
};
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


// TODO: 在此处引用程序需要的其他头文件
