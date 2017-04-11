#ifndef LINKS_TREE_H_INCLUDED
#define LINKS_TREE_H_INCLUDED
#include <iostream>
using namespace std;
template<class DataType>
class BiThreTree
{
private:
    enum PointerTag{Link, Thread};
    PointerTag LTag, RTag;
    DataType data;
    BiThreTree *lchild, *rchild;
public:
    BiThreTree *CreateTree();
    void InThread();
    BiThreTree *InOrderThrTree();
    void InThrTravel();
    void FirstThrTravel();
    void LastThrTravel();
    void Last();
};
//template<class DataType>
BiThreTree<char> *pre;//全局变量,用于二叉树的线索化

template<class DataType>
void BiThreTree<DataType>::LastThrTravel()//递归实现后序遍历线索二叉树
{
    lchild->Last();
}


template<class DataType>
BiThreTree<DataType> *BiThreTree<DataType>::CreateTree()               //按先序输入建立二叉树
{
    BiThreTree *T;
    DataType e;
   // scanf("%d", &e);
    cin>>e;
    if(e=='.')
        T=NULL;
    else
    {
        T=new BiThreTree;
        T->data=e;
        T->LTag=Link;        //初始化时指针标志均为Link
        T->RTag=Link;
        T->lchild=CreateTree();
        T->rchild=CreateTree();
    }
    return T;
}//

template<class DataType>
void BiThreTree<DataType>::InThread()//对二叉树进行中序线索化的算法
{
    BiThreTree *p;
    p=this;
    if(p)
    {
        p->lchild->InThread();//左子树线索化
        if(!p->lchild)//if左子树为空
        {
            p->LTag=Thread;
            p->lchild=pre;
        }
        if(!pre->rchild)//if右子树为空
        {
            pre->RTag=Thread;
            pre->rchild=p;
        }
        pre=p;//前驱指向当前节点
        p->rchild->InThread();//右子树线索化
    }
}//

template<class DataType>
BiThreTree<DataType> *BiThreTree<DataType>::InOrderThrTree()//中序线索化二叉树
{
    BiThreTree *Thre;      //Thre为头结点的指针
    Thre=new BiThreTree;
    Thre->lchild=this;
    Thre->rchild=Thre;
    pre=Thre;
    this->InThread();
    pre->RTag=Thread;
    pre->rchild=Thre;
    Thre->rchild=pre;
    return Thre;
}//

template<class DataType>
void BiThreTree<DataType>::InThrTravel()//中序遍历二叉树
{
    BiThreTree *p;
    p=lchild;//Thre是树的"头"
    while(p!=this)//指针回指向头结点时结束
    {
        while(p->LTag==Link)//Link表示子树存在
            p=p->lchild;
            cout<<" "<<p->data;
        //printf("%4d", p->data);
        while(p->RTag==Thread&&p->rchild!=this)//Thread表示右子树不存在,存在的"右子树"是线索
        {
            p=p->rchild;
             cout<<" "<<p->data;
        //printf("%4d", p->data);
        }
        p=p->rchild;
    }
}//
template<class DataType>
void BiThreTree<DataType>::FirstThrTravel()//先序遍历线索二叉树
{
    BiThreTree *p;
    p=lchild;
    while(p!=this)
    {
         cout<<" "<<p->data;
        //printf("%4d", p->data);
        while(p->LTag==Link)
        {
            p=p->lchild;
             cout<<" "<<p->data;
        //printf("%4d", p->data);
        }
        if(p->RTag==Thread)
            {p=p->rchild;}
        if(p->RTag==Link)
            {p=p->rchild;}
    }
}//
template<class DataType>
void BiThreTree<DataType>::Last()//递归实现后序遍历线索二叉树
{
    if(this!=NULL)
    {
        if(LTag==Link)
            lchild->Last();
        if(RTag==Link)
            rchild->Last();
         cout<<" "<<data;
        //printf("%4d", data);
    }

}



#endif // LINKS_TREE_H_INCLUDED
