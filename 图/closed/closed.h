#ifndef CLOSED_H_INCLUDED
#define CLOSED_H_INCLUDED
#include <iostream>
#include <math.h>
#define max_number 100000
using namespace std;
template<class T>
class closed
{
public:
    closed();
    ~closed()
    {
    }
    void display();
    void insrt(T x);
    bool found(T x);
    void init();
private:
    int h(T x);
    T table[max_number];
    bool flag[max_number];
    int num,total;
    bool IsPrime(int value);
};
template <class T>
void closed<T>::init()
{
    total=0;
    cout<<"input the number of the data in:";
    cin>>num;
    while(!IsPrime(num))
    {
        num--;
    }
    for(int i=0;i<num;i++)
    {
        flag[i]=false;
    }
    cout<<"prime is:"<<num<<endl;
}
template <class T>
int closed<T>::h(T x)
{
    return x%num;
}
template <class T>
bool closed<T>::found(T x)
{
    int n=h(x),times=1;
    while(flag[n]&&times<=num+1)
    {
        if(x==table[n])
        {
            cout<<"data "<<x<<" located in "<<n<<"   compared times:"<<times<<endl;
            return true;
        }
        times++;
        n=(++n)%num;
    }
    return false;
}
template <class T>
void closed<T>::insrt(T x)
{
    cout<<"insert "<<x<<":"<<endl;
    if(total==num)
    {
        cout<<"full"<<endl;
        return;
    }
    else if(found(x))
    {
        cout<<"the data has already exist."<<endl;
    return ;
    }
    else
    {

    int n=h(x);
    while(flag[n])
    {
        n=(++n)%num;
        cout<<n<<endl;
    }
    total++;
    table[n]=x;
    flag[n]=true;
    return ;
    }
}
template<class T>
void closed<T>::display()
{
    cout<<"table length:"<<num<<endl;
    cout<<"closed_hash table:"<<endl;
   for(int i=0;i<num;i++)
   {
       cout<<i<<":";
       if(flag[i]) cout<<table[i];
        cout<<endl;
   }
}
template<class T>
closed<T>::closed()
{
    total=0;
    cout<<"input the number of the data in:";
    cin>>num;
    while(!IsPrime(num))
    {
        num--;
    }
    for(int i=0;i<num;i++)
    {
        flag[i]=false;
    }
    cout<<"prime is:"<<num<<endl;
}
template <class T>
bool closed<T>::IsPrime(int value)
{
    if(value%2)
    {
        for(int i=3; i<=(int)sqrt((float)value); i+=2)
        {
            if(value%i==0)
                return false;
        }
        return true;
    }
    return false;
}
#endif // CLOSED_H_INCLUDED
