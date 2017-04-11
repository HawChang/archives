#ifndef ISPRIME_H_INCLUDED
#define ISPRIME_H_INCLUDED
#include <math.h>
#include <iostream>
using namespace std;

class OpenHash
{
public:
    OpenHash();
    ~OpenHash()
    {
        delete []data;
    }
    void Init();
    int h(int key);
    void Insert(int key);
    bool Search(int key);
    void display();
//    void check();
private:
    struct node
    {
        int value;
        node *next;
    };
    int maxSize;
    node *data;
    bool *flag;
    int prime;
    bool IsPrime(int value);
};
bool OpenHash::IsPrime(int value)
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
//void OpenHash::check()
//{
//    prime=1;
//    node *p=new node;
//    node *q=new node;
//    data[0].next=p;
//    p->next=q;
//    data[0].value=0;
//    p->value=1;
//    q->value=2;
//    flag[0]=true;
//    q->next=NULL;
//}
void OpenHash::display()
{
    node *temp;
    cout<<"table length:"<<prime<<endl;
    cout<<"hash table:"<<endl;
    for(int i=0; i<prime; i++)
    {
        cout<<i<<":";
        if(flag[i])
        {
            temp=&data[i];
            while(temp)
            {
                //cout<<temp->value<<endl;
                cout<<temp->value<<" ";
                temp=temp->next;
                // cout<<temp->value<<endl;
            }
        }
        cout<<endl;
    }
}
OpenHash::OpenHash()
{
    cout<<"input the number of the data in:";
    cin>>maxSize;
    data=new node[maxSize];
    flag=new bool[maxSize];
    prime=maxSize;
    while(!IsPrime(prime))
    {
        prime--;
    }
    cout<<"prime is:"<<prime<<endl;
}
void OpenHash::Init()
{
    for(int i=0; i<maxSize; i++)
    {
        data[i].next=NULL;
        flag[i]=false;
    }
}
int OpenHash::h(int key)
{
    return key%prime;
}
void OpenHash::Insert(int key)
{
    cout<<"insert "<<key<<":"<<endl;
    if(Search(key)) cout<<"the data has already exist."<<endl;
    else
    {
        int pos=h(key);
        if(flag[pos])
        {
            node *p=&data[pos];
            node *q=new node;
            while(p->next!=NULL)
            {
                p=p->next;
            }
            p->next=q;
            q->value=key;
            q->next=NULL;
        }
        else
        {
            data[pos].value=key;
            flag[pos]=true;
        }
    }
}
bool OpenHash::Search(int key)
{
    int pos=h(key);
    if(data[pos].value!=key)
    {
        node *p=data[pos].next;
        int number=1;
        while(p!=NULL)
        {
            if(p->value==key)
            {
                cout<<"located in pos #"<<pos<<",compared times:"<<number<<endl;
                return true;
            }
            p=p->next;
            number++;
        }
    }
    else
    {
        cout<<"located in pos #"<<pos<<endl;
        return true;
    }
    return false;
}

#endif // ISPRIME_H_INCLUDED
