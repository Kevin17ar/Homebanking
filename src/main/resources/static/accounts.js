window.addEventListener('load', () => {
    const contenedor_loader = document.querySelector('.contenedor')
    contenedor_loader.style.opacity = 0
    contenedor_loader.style.visibility = 'hidden'
})

const app = Vue.createApp({
    data() {
        return {
            cliente: [],
            cuentas: [],
            prestamos: [],
            accountSelect: "",
            typeSelect: "",
        }
    },
    created() {
        axios.get("/api/clients/current")
            .then(res => {
                this.cliente = res.data
                this.cuentas = res.data.accounts.filter(cuenta => cuenta.active);
                this.prestamos = res.data.loans

                console.log(this.cliente)
                console.log(this.cuentas)
                console.log(this.prestamos)
            })
            .catch(err => console.log(err))
    },
    methods: {
        signOut() {
            axios.post('/api/logout')
                .then(response => {
                    console.log("out")
                    location.href = "/index.html"
                })
        },
        createAccount() {
            axios.post("/api/clients/current/accounts", "accountType=" + this.typeSelect)
                .then(res => {
                    swal({
                        title: "Congratulations",
                        text: "You can sign in whith your email and password",
                        icon: "success",
                        button: "OK",
                    })
                })
                .catch(err => {
                    swal({
                        title: "Alert",
                        text: "You have 3 accounts, to request more contact customer service",
                        icon: "warning",
                    });
                })
        },
        deleteAccount() {
            swal({
                    title: "Are you sure?",
                    icon: "warning",
                    buttons: true,
                    dangerMode: true,
                })
                .then((willDelete) => {
                    if (willDelete) {
                        axios.post('/api/clients/current/account/delete', "id=" + this.accountSelect)
                            .then(response => {
                                swal({
                                    title: "Congratulations",
                                    text: "Account delete",
                                    icon: "success",
                                    button: "OK",
                                });
                                location.reload();
                            })
                            .catch(err => {
                                swal({
                                    title: "Alert",
                                    text: "Check the data entered",
                                    icon: "warning",
                                })
                            })
                    } else {
                        swal("for question contac us");
                    }
                });
        },
    },
    computed: {
        // url(index) {
        //     console.log(index)
        // }
    },
})
app.mount("#app")