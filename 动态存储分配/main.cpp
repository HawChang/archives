#include <iostream>
#define maxlength 1000
using namespace std;
typedef int position;
template<class T>
class node
{
public:
    T element;
    position next;
};
template<class T>
class heep
{
public:
    heep()
    {
        int i;
        for(i=0; i<maxlength-1; i++)
        {
            space[i].next=i+1;
        }
        space[i].next=-1;
        available=0;
    }
    node<T> space[maxlength];
    position available;
    position newspace();
    void delone(position n,position q);
    void insrt(T x,position n,position p);
    void delline(position q);
    void display(position q);
};
heep<char> *h=new heep<char>;
template <class T>
void heep<T>::display(position q)
{
    for(;h->space[q].next!=-1;)
    {
        q=h->space[q].next;
        cout<<h->space[q].element;
    }
    cout<<endl;
}
template <class T>
void heep<T>::delline(position q)
{
    position p;
    if(h->space[q].next!=-1)
    {
        p=h->space[q].next;
        h->space[q].next=h->space[p].next;
        h->delone(1,p);
    }
}
template <class T>
void heep<T>::insrt(T x,position n,position p)
{
    position q;
    q=h->newspace();
    h->space[q].element=x;
    while(--n&&h->space[p].next!=-1)
    {
        p=h->space[p].next;
    }
    if(h->space[p].next==-1)
    {
        h->space[p].next=q;
    }
    else
    {
        h->space[q].next=h->space[p].next;
        h->space[p].next=q;
    }
}
template <class T>
void heep<T>::delone(position n,position q)
{
    position temp;
    while(--n>=1&&h->space[q].next!=-1)
    {
        q=h->space[q].next;
    }
    if(h->space[q].next==-1);
    else
    {
        temp=h->space[q].next;
        h->space[q].next=h->space[h->space[q].next].next;
        temp=h->space[h->available].next;
        h->space[h->available].next=temp;
    }
}
template <class T>
position heep<T>::newspace()
{
    position q;
    if(h->space[h->available].next==-1)
    {
        q=-1;
        return q;
    }
    else
    {
        q=h->space[h->available].next;
        h->space[h->available].next=h->space[q].next;
        h->space[q].next=-1;
        return q;
    }
}
int main()
{
    int pos,choose;
    position q;
    char a[maxlength],c;
    cout<<"input the orgin string:";
    cin>>a;
    q=h->newspace();
    for(int i=0; a[i]!='\0'; i++)
    {
        h->insrt(a[i],i+1,q);
    }
    while(1)
    {
        cout<<"--------------choose the functions------------------------"<<endl;
        cout<<"1.insrt             2.del               3.display         "<<endl;
        cout<<"----------------------------------------------------------"<<endl;
        cin>>choose;
        switch(choose)
        {
        case(1):
            cout<<"input the position you want to insrt and the charactor:";
            cin>>pos>>c;
            h->insrt(c,pos,q);
            h->display(q);
            break;
        case(2):
            cout<<"input the position you want to delete:";
            cin>>pos;
            h->delone(pos,q);
            h->display(q);
            break;
        case(3):
            h->display(q);
            break;
        default:
            return 0;
        }
    }
    return 0;
}
