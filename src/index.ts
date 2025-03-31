import { PrismaClient } from '@prisma/client';

const prisma = new PrismaClient();

// create a new user
await prisma.user.create({
    data: {
        fullname: 'John Dough',
        email: `john-${Math.random()}@example.com`,
        password: 'password',
    },
});

console.log(
    await prisma.user.findMany({
        where: {
            fullname: 'John Dough',
        },
    }),
);

// count the number of users
const count = await prisma.user.count();
console.log(typeof prisma.user);
console.log(prisma.user.fields);
console.log(`There are ${count} users in the database.`);
