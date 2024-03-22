// IpPortLink.cpp: implementation of the CIpPortLink class.
//
//////////////////////////////////////////////////////////////////////

#include "stdafx.h"
#include "IP1portFind.h"

//////////////////////////////////////////////////////////////////////
// Construction/Destruction
//////////////////////////////////////////////////////////////////////

CIP1portFind::CIP1portFind()
{
	m_size    = 0;
	m_hashlen = 256;
	m_HashTable=new FindNumNode*[m_hashlen];
	memset(m_HashTable,0,sizeof(FindNumNode*)*m_hashlen);
}

CIP1portFind::CIP1portFind(int len)
{
	m_size = 0;
	m_hashlen=len;
	m_HashTable=new FindNumNode*[m_hashlen];
	memset(m_HashTable,0,sizeof(FindNumNode*)*m_hashlen);
}

CIP1portFind::~CIP1portFind()
{
	Close();
	delete []m_HashTable;
}

void CIP1portFind::Close()
{
	FindNumNode *p1,*p2;
	for(int i=0;i<m_hashlen;i++)
	{
		p1 = m_HashTable[i];
		while(p1)
		{   
			p2 =p1;
			p1 = p1->next;
			delete p2;
		}
		m_HashTable[i] = NULL;
	}
	m_size=0;
}

BOOL CIP1portFind::Add(WORD srcport, DWORD srcip, DWORD value)
{
	DWORD item=Hash(srcip,srcport);
	FindNumNode	*pNode = m_HashTable[item];	

	while(pNode)
	{
		if (pNode->srcip==srcip&&pNode->port==srcport)
		{
			return FALSE;
		}
		pNode = pNode->next;
	}

	pNode = new FindNumNode;
	pNode->port    =srcport;
	pNode->srcip   =srcip;
	pNode->value  = value;

	pNode->next = m_HashTable[item];
	m_HashTable[item] = pNode;
	m_size ++ ;
	return TRUE;
}

BOOL CIP1portFind::Del(WORD srcport, DWORD srcip)
{
	DWORD item=Hash(srcip,srcport);
	FindNumNode	*pNode = m_HashTable[item];	
	FindNumNode *pre = pNode;
	
	while(pNode)
	{
		if (pNode->port==srcport&&pNode->srcip==srcip)
		{
			if(pre==pNode)
			{
				m_HashTable[item] = pre->next;				
			}
			else
			{
				pre->next = pNode->next;
			}			
			delete pNode;
			m_size--;		
			return TRUE;
		}
		pre = pNode;
		pNode = pNode->next;		
	}
	return FALSE;
}
