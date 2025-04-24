export type User = {
    id: string;
    name: string;
    email: string;
    role: string; // 'ADMIN', 'MANAGER', 'MEMBER'
    createdAt: Date;
    updatedAt: Date;
    };