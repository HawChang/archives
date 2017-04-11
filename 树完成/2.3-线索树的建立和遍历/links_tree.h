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
BiThreTree<char> *pre;//ȫ�ֱ���,���ڶ�������������

template<class DataType>
void BiThreTree<DataType>::LastThrTravel()//�ݹ�ʵ�ֺ����������������
{
    lchild->Last();
}


template<class DataType>
BiThreTree<DataType> *BiThreTree<DataType>::CreateTree()               //���������뽨��������
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
        T->LTag=Link;        //��ʼ��ʱָ���־��ΪLink
        T->RTag=Link;
        T->lchild=CreateTree();
        T->rchild=CreateTree();
    }
    return T;
}//

template<class DataType>
void BiThreTree<DataType>::InThread()//�Զ����������������������㷨
{
    BiThreTree *p;
    p=this;
    if(p)
    {
        p->lchild->InThread();//������������
        if(!p->lchild)//if������Ϊ��
        {
            p->LTag=Thread;
            p->lchild=pre;
        }
        if(!pre->rchild)//if������Ϊ��
        {
            pre->RTag=Thread;
            pre->rchild=p;
        }
        pre=p;//ǰ��ָ��ǰ�ڵ�
        p->rchild->InThread();//������������
    }
}//

template<class DataType>
BiThreTree<DataType> *BiThreTree<DataType>::InOrderThrTree()//����������������
{
    BiThreTree *Thre;      //ThreΪͷ����ָ��
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
void BiThreTree<DataType>::InThrTravel()//�������������
{
    BiThreTree *p;
    p=lchild;//Thre������"ͷ"
    while(p!=this)//ָ���ָ��ͷ���ʱ����
    {
        while(p->LTag==Link)//Link��ʾ��������
            p=p->lchild;
            cout<<" "<<p->data;
        //printf("%4d", p->data);
        while(p->RTag==Thread&&p->rchild!=this)//Thread��ʾ������������,���ڵ�"������"������
        {
            p=p->rchild;
             cout<<" "<<p->data;
        //printf("%4d", p->data);
        }
        p=p->rchild;
    }
}//
template<class DataType>
void BiThreTree<DataType>::FirstThrTravel()//�����������������
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
void BiThreTree<DataType>::Last()//�ݹ�ʵ�ֺ����������������
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
