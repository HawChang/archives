#include <iostream>
#include "heaporder.h"
using namespace std;
int main()
{
    int choose,n,input;
    heap<int> *h=new heap<int>;
    while(1)
    {
        cout<<"the number of the inputs: ";
        cin>>n;
        for(int i=0; i<n; i++)
        {
            cout<<"number #"<<i+1<<":";
            cin>>input;
            if(h->insrt(h,input));
            else cout<<"input fail, the heap is full."<<endl;
        }
        cout<<"the order up to down is:";
        for(int i=0;i<n;i++)
        {
            cout<<h->maxout(h)<<"  ";
        }
        cout<<endl;
    }
    return 0;
}
