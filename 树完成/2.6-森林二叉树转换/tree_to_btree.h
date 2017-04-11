#ifndef TREE_TO_BTREE_H_INCLUDED
#define TREE_TO_BTREE_H_INCLUDED
#define maxlength 1000
using namespace std;

template<class T>
class adjtable
{
    struct point_child
    {
        int child;
        struct point_child *next;
    };
    struct node
    {
        T data;
        point_child* firstchild;
    };
    node a[maxlength];
    int node_number;
    int root;
    void d_insrt(int i,T x);
public:
    adjtable()
    {
        for(int i=0; i<maxlength; i++)
            a[i].firstchild=nullptr;
    };
    ~adjtable() {};
    void create();
    void insrt(int i,T x);
    int found(T x);
    void display();
    void trans();
};
template<class T>
void adjtable<T>::trans()
{

    point_child* p;
    point_child* temp[maxlength];
    int length=0;
    for(int i=0; i<node_number; i++)
    {
        if(a[i].firstchild)
        {
            temp[length++]=a[i].firstchild;
        }
    }
    for(int i=0; i<length; i++)
    {
        p=temp[i];
        while(p->next)
        {
            d_insrt(p->child,a[p->next->child].data);
            p=p->next;
        }
    }
    for(int i=0; i<length; i++)
    {
        temp[i]->next=nullptr;
    }
}
template<class T>
void adjtable<T>::d_insrt(int i,T x)
{
    int m=found(x);
    if(m==-1)
    {
        cout<<"doesn't find the parent."<<endl;
    }
    else
    {
        point_child *f=new point_child;
        f->child=m;
        f->next=a[i].firstchild;
        a[i].firstchild=f;
    }
    return ;
}
template<class T>
void adjtable<T>::display()
{
    point_child *temp;
    for(int i=0; i<node_number; i++)
    {
        cout<<"charactor:"<<a[i].data;
        temp=a[i].firstchild;
        while(temp)
        {
            cout<<"  son:"<<a[temp->child].data;
            temp=temp->next;
        }
        cout<<endl;
    }
}
template<class T>
int adjtable<T>::found(T x)
{
    for(int i=0; i<node_number; i++)
    {
        if(x==a[i].data)
            return i;
    }
    return -1;
}
template<class T>
void adjtable<T>::insrt(int i,T x)
{
    int m=found(x);
    if(m==-1)
    {
        cout<<"doesn't find the parent."<<endl;
    }
    else
    {
        point_child *f=new point_child;
        point_child *temp=a[i].firstchild;
        if(temp)
        {
            while(temp->next)
            {
                temp=temp->next;
            }
            f->next=temp->next;
            temp->next=f;
            f->child=m;
        }
        else
        {
            f->child=m;
            f->next=nullptr;
            a[i].firstchild=f;
        }
    }
    return ;
}
template<class T>
void adjtable<T>::create()
{
    T temp;
    int father,son,number,flag;
    cout<<"input the number of the nodes:";
    cin>>node_number;
    for(int i=0; i<node_number; i++)
    {
        flag=1;
        cout<<"input the data of the node #"<<i+1<<":";
        cin>>temp;
        for(int j=0; j<i; j++)
        {
            if(temp==a[j].data)
            {
                cout<<"the node has already existed."<<endl;
                flag=0;
                i--;
            }
        }
        if(flag) a[i].data=temp;
    }
    for(int i=0; i<node_number; i++)
    {
        cout<<"input the number of the node "<<a[i].data<<"'s son:"<<endl;
        cin>>number;
        for(int j=0; j<number; j++)
        {
            cout<<"input the son #"<<j+1<<":";
            cin>>temp;
            insrt(i,temp);
            cout<<"success"<<endl;
        }
    }
}


#endif // TREE_TO_BTREE_H_INCLUDED
