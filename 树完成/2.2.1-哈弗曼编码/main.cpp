#include <iostream>
#include <fstream>
#include <string.h>
#define yimamax 10000
#define ciphermax 10000
#define strmax 10000
using namespace std;
int a[128],coun=0,p1,p2;
char temp[128]="";
char *zero="0",*one="1";
class huffcode
{
public:
    char code[128];
    int length;
};
class translate
{
public:
    huffcode trans[128];
    translate()
    {
        for(int i=0; i<128; i++)
        {
            strcpy(trans[i].code,"");
            trans[i].length=0;
        }
    }
};
class htnode
{
public:
    int character;
    int weight;
    int lchild;
    int rchild;
    int parent;
};
class huffman
{
public:
    htnode t[255];
    void match(int number,char str[]);
    void selectmin(huffman *tree,int n);
    void init(huffman *tree,char b[][2]);
    void bianma(huffman *tree,translate *tr,int n);
    void writehuffmancodestable(huffman *tree,translate *tr);
    void writecipher(huffman *tree,translate *tr,char str[]);
    void yima(int cinumber,char cipherstr[],char yimastr[],translate *tr);
};
void huffman::match(int number,char str[])             //���ļ��г��ֵ��ַ���¼��str�����У�str[i]=n��ʾascll��Ϊi���ַ����ļ��г���n�Ρ�
{
    int i;
    for(i=0; i<number; i++)
    {
        a[str[i]]++;
    }
}
void huffman::selectmin(huffman *tree,int n)                //��huffman�������ݹ齫��ʱ��С�����ڵ���Ϊ���Ҷ������ɸ����ֱ��ʣ��һ���ڵ㡣
{
    int i,j;
    for(i=0; i<=n; i++)
        if(tree->t[i].parent == -1)
        {
            p1 = i;
            break;
        }
    for(j=i+1; j <= n; j++)
        if(tree->t[j].parent == -1)
        {
            p2 = j;
            break;
        }
    for(i =0; i <= n; i++)
        if((tree->t[p1].weight > tree->t[i].weight) && (tree->t[i].parent == -1)&& (p2 != i))
            p1=i;
    for(j = 0; j <=n; j++)
        if((tree->t[p2].weight > tree->t[j].weight) && (tree->t[j].parent == -1)&& (p1 != j))
            p2=j;
}
void huffman::init(huffman *tree,char b[][2])
{
    int i;
    for(i=0; i<2*coun-1; i++)
    {
        tree->t[i].lchild=-1;
        tree->t[i].parent=-1;
        tree->t[i].rchild=-1;
    }
    for(i=0; i<coun; i++)
    {
        tree->t[i].weight=b[i][1];
        tree->t[i].character=b[i][0];
    }
    for(i=coun; i<2*coun-1; i++)
    {
        tree->selectmin(tree,i-1);
        tree->t[p1].parent=tree->t[p2].parent=i;
        tree->t[i].lchild=p1;
        tree->t[i].rchild=p2;
        tree->t[i].weight=tree->t[p1].weight+tree->t[p2].weight;
    }
}
void huffman::bianma(huffman *tree,translate *tr,int n)       //�Ӹ����ݹ�huaffman����������㼴��¼�õ��huffman���룬translate[i].code��ʾascll��Ϊi��huffman���룬��ֻ��translate[i].length��Ϊ�����Huffman����
{

    if(tree->t[n].lchild!=-1)
    {
        strcat(temp,zero);
        tree->bianma(tree,tr,tree->t[n].lchild);
    }
    if(tree->t[n].rchild!=-1)
    {
        strcat(temp,one);
        bianma(tree,tr,tree->t[n].rchild);
    }
    if(tree->t[n].rchild==-1&&tree->t[n].lchild==-1)
    {
        strcpy(tr->trans[tree->t[n].character].code,temp);
        tr->trans[tree->t[n].character].length=strlen(temp);
    }
    temp[strlen(temp)-1]='\0';
}
void huffman::writehuffmancodestable(huffman *tree,translate *tr)          //��huffman�����д��D:\\huffmancodestable.txt����ͬ��ֻ���ļ��д��ڵ��ַ��б���
{
    char file[strmax];
    FILE *fp;
    cout<<"input the file which you want the codestable in:"<<endl<<">>";
    cin>>file;
    fp=fopen(file,"w");
    for(int i=0; i<128; i++)
    {
        if(tr->trans[i].length)
        {
            fprintf(fp,"%c:%s\n",i,tr->trans[i].code);
        }
    }
    fclose(fp);
}
void huffman::writecipher(huffman *tree,translate *tr,char str[])                    //��ԭ�ļ�ת��Ϊhuffman���뵽D:\\cipher.txt
{
    int n=-1;
    char file[strmax];
    FILE *fp;
    cout<<"input the file which you want the huffman_code_file in:"<<endl<<">>";
    cin>>file;
    fp=fopen(file,"w");
    while(str[++n])
    {
        fprintf(fp,"%s",tr->trans[str[n]].code);
    }
    fclose(fp);
}
void huffman::yima(int cinumber,char cipherstr[],char yimastr[],translate *tr)    //��huffman�����ļ�תΪΪ�ַ��ļ���D:\\yima.txt
{
    char cmp[128];
    int t=0,s=0;
    for(int i=0; i<cinumber; i++)
    {
        cmp[t++]=cipherstr[i];
        cmp[t]='\0';
        for(int j=0; j<128; j++)
        {
            if(!strcmp(tr->trans[j].code,cmp))
            {
                yimastr[s++]=j;
                strcpy(cmp,"");
                t=0;
                break;
            }
        }
    }
    yimastr[s]='\0';
    char file[strmax];
    FILE *fp;
    cout<<"input the file which you want the trans of the huffman_code_file in:"<<endl<<">>";
    cin>>file;
    fp=fopen(file,"w");
    fprintf(fp,"%s",yimastr);
    fclose(fp);
}
int main()
{
    int choose,i;
    char source[strmax],str[strmax],cipherstr[ciphermax],yimastr[yimamax],b[128][2];
    translate *tr=new translate;
    huffman *tree=new huffman;//(huffman *)malloc(sizeof(huffman));
    cout<<"-------------------------------------------------------------------"<<endl;
    cout<<"-----choose functions:---------------------------------------------"<<endl;
    cout<<"--1.translate file into huffman code   2.translate back------------"<<endl;
    cout<<"-------------------------------------------------------------------"<<endl;
    while(cin>>choose)
    {
        fstream file;
        switch(choose)
        {
        case 1:
            i=0;
            cout<<"input the file you want to translate in to huffman:";
            cin>>source;
            file.open(source);
            if(file.fail())
            {
                cout<<"ʧ�ܡ�\n"<<endl;
                return 0;
            }
            while(!file.eof())
            {
                str[i++]=file.get();
            }
            str[i]='\0';
            i=strlen(str);                         //��ȡ�ļ�
            cout<<str<<endl;
            cout<<i-1<<"words"<<endl;
            tree->match(i,str);//ƥ���ַ�
            file<<"END";
            file.close();
            for(i=0; i<128; i++)
            {
                if(a[i])
                {
                    b[coun][0]=i;
                    b[coun++][1]=a[i];       //b[coun][0]������ַ���b[coun][1]�����ַ�Ȩֵ��coun-1�����ж������ַ�
                }
            }
            tree->init(tree,b);
            tree->bianma(tree,tr,2*(coun-1));                        //�Ӹ��ڵ����
            tree->writehuffmancodestable(tree,tr);                   //��huffman�����д���ļ�
            tree->writecipher(tree,tr,str);      //��ԭ�ļ�д��huffman����
            printf("\n(1).�����ļ��г��ֵ��ַ���Ȩֵ��\n");
            for(i=0; i<coun; i++)
            {
                printf("%03d %c:%d\n",b[i][0],b[i][0],b[i][1]);
            }
            printf("\n(2).�����huffman����\n");
            for(i=0; i<2*coun-1; i++)
            {
                printf("%d:character:%c     weight:%d      lchild:%d       rchild:%d     parent:%d \n",i,b[i][0],tree->t[i].weight,tree->t[i].lchild,tree->t[i].rchild,tree->t[i].parent);
            }
            printf("\n(3).�ַ���Ӧ��huffman���룺\n");
            for(int i=0; i<128; i++)
            {
                if(tr->trans[i].length)
                {
                    printf("%c:%s\n",i,tr->trans[i].code);
                }
            }
//            printf("\n(4).��ԭ�ļ�ת��Ϊhuffman�����ļ���\n");
//            i=-1;
//            while(str[++i])
//            {
//                printf("%s",tr->trans[str[i]].code);
//            }
//            cout<<endl;
            break;
        case 2:
            cout<<"input the file you want to trans back"<<endl<<">>";
            cin>>source;
            file.open(source);                //��ȡhuffman�����ļ�
            if(file.fail())
            {
                cout<<"ʧ�ܡ�\n"<<endl;
                return 0;
            }
            i=0;
            while(!file.eof())
            {
                cipherstr[i++]=file.get();
            }
            cipherstr[i]='\0';
            i=strlen(cipherstr);
            tree->yima(i,cipherstr,yimastr,tr);                                   //��huffman�����ļ�������ַ��ļ�
            file<<"END";
            file.close();
            printf("\n(5).huffman�����ļ�ת����ԭ�����ַ��ļ���\n");
            cout<<yimastr<<endl;
            break;
        default:
            return 0;
        }
        cout<<"-------------------------------------------------------------------"<<endl;
        cout<<"-----choose functions:---------------------------------------------"<<endl;
        cout<<"--1.translate file into huffman code   2.translate back------------"<<endl;
        cout<<"-------------------------------------------------------------------"<<endl;
    }
    return 0;
}
