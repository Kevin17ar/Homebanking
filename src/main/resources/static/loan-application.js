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
            cuotas: [],
            typeLoan: null,
            amount: null,
            number: "",
            payment: "",
        }
    },
    created() {
        axios.get("/api/clients/current")
            .then(res => {
                this.cliente = res.data
                this.cuentas = res.data.accounts.filter(cuenta => cuenta.active)
                console.log(this.cliente)
            })
            .catch(err => console.log(err))

        axios.get("/api/loans")
            .then(res => {
                this.prestamos = res.data
                console.log(this.prestamos)
                console.log(this.hipetecario)
            })
            .catch(err => console.log(err))
    },
    methods: {
        signOut() {
            axios.post('/api/logout')
                .then(response => {
                    location.href = "http://localhost:8686/index.html"
                })
        },
        createLoan() {
            swal({
                    title: "Are you sure?",
                    icon: "warning",
                    buttons: true,
                    dangerMode: true,
                })
                .then((willDelete) => {
                    if (willDelete) {
                        axios.post('/api/clients/current/loans', {
                                id: this.typeLoan,
                                amount: this.amount,
                                payments: this.payment,
                                number: this.number,
                            })
                            .then(response => {
                                swal({
                                    title: "Congratulations",
                                    text: "amount transferred to selected account",
                                    icon: "success",
                                    button: "OK",
                                });
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
        payments() {

            let payList = this.prestamos.filter(prestamo => prestamo.id == this.typeLoan)
            let list = [];
            payList.forEach(element => {
                list.push(element.payments)
            })
            return list;
        },
        interest() {
            let payList = this.prestamos.filter(prestamo => prestamo.id == this.typeLoan)
            let list = []
            payList.forEach(element => {
                list.push(element.interest)
            })
            return list;
            // if (this.typeLoan == 1) {
            //     return 15.33
            // }
            // if (this.typeLoan == 2) {
            //     return 22
            // }
            // if (this.typeLoan == 3) {
            //     return 33.5
            // }
        },
    },
})
app.mount("#app")